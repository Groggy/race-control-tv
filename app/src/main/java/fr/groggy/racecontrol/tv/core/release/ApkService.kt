package fr.groggy.racecontrol.tv.core.release

import android.app.DownloadManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_INTENT
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentFilter
import android.content.pm.PackageInstaller.*
import android.content.pm.PackageInstaller.SessionParams.MODE_FULL_INSTALL
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.BuildConfig.APPLICATION_ID
import fr.groggy.racecontrol.tv.ui.MainActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.FileInputStream
import java.lang.Exception
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class ApkService : Service() {

    companion object {

        private val TAG = ApkService::class.simpleName

        private const val ACTION_INSTALL = "INSTALL"
        private const val ACTION_INSTALL_RESULT = "INSTALL_RESULT"

        private const val EXTRA_APK = "APK"

        fun installIntent(context: Context, apk: Apk): Intent {
            val intent = Intent(context, ApkService::class.java)
            intent.action = ACTION_INSTALL
            intent.putExtra(EXTRA_APK, apk)
            return intent
        }

        fun findApk(intent: Intent): Apk? =
            intent.getParcelableExtra(EXTRA_APK)

        fun installResultIntent(context: Context): Intent {
            val intent = Intent(context, ApkService::class.java)
            intent.action = ACTION_INSTALL_RESULT
            return intent
        }

    }

    private val executor by lazy { Executors.newSingleThreadExecutor() }

    private val packageInstaller by lazy { packageManager.packageInstaller }

    @Inject lateinit var downloadManager: DownloadManager
    @Inject lateinit var localBroadcastManager: LocalBroadcastManager

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        when(intent.action) {
            ACTION_INSTALL -> {
                val apk = findApk(intent)!!
                executor.submit { downloadAndStartInstall(apk) }
            }
            ACTION_INSTALL_RESULT -> {
                onInstallResult(intent)
                stopSelf()
            }
            else -> {
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun downloadAndStartInstall(apk: Apk) {
        try {
            val fileDescriptor = runBlocking { download(apk) }
            val session = openInstallSession()
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                session.openWrite(apk.name, 0, fileDescriptor.statSize).use { outputStream ->
                    inputStream.copyTo(outputStream)
                    session.fsync(outputStream)
                }
            }
            val callbackIntent = installResultIntent(applicationContext)
            val pendingIntent = PendingIntent.getService(applicationContext, 0, callbackIntent, 0)
            session.commit(pendingIntent.intentSender)
        }
        catch (e: Exception) {
            Log.e(TAG, "Fail to download and instal new version", e)
            val intent = MainActivity.installResultBroadcastIntent(null)
            localBroadcastManager.sendBroadcast(intent)
        }
        stopSelf()
    }

    private suspend fun download(apk: Apk): ParcelFileDescriptor {
        Log.d(TAG, "Downloading ${apk.url}")
        val id = downloadAndWaitForCompletion(apk)
        Log.d(TAG, "Download terminated")
        val fileDescriptor = findDownloadedFile(id )
            ?: throw ApkDownloadFailedException(apk)
        Log.d(TAG, "APK downloaded")
        return fileDescriptor
    }

    private suspend fun downloadAndWaitForCompletion(apk: Apk): Long {
        val downloadedIds = registerDownloadCompleteReceiver()
        try {
            val id = enqueueDownload(apk)
            waitDownloadComplete(downloadedIds, id)
            return id
        } finally {
            downloadedIds.close()
        }
    }

    private fun registerDownloadCompleteReceiver(): Channel<Long> {
        val channel = Channel<Long>()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                channel.offer(id)
            }
        }
        channel.invokeOnClose { unregisterReceiver(receiver) }
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        return channel
    }

    private suspend fun waitDownloadComplete(downloadedIds: Channel<Long>, id: Long) {
        downloadedIds.consumeAsFlow()
            .filter { it == id }
            .first()
    }

    private fun enqueueDownload(apk: Apk): Long {
        val request = DownloadManager.Request(apk.url)
        request.setTitle(apk.name)
        return downloadManager.enqueue(request)
    }

    private fun findDownloadedFile(id: Long): ParcelFileDescriptor? {
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor = downloadManager.query(query)
        if (!cursor.moveToFirst()) {
            return null
        }
        val columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val status = cursor.getInt(columnStatusIndex)
        return if (status == DownloadManager.STATUS_SUCCESSFUL) {
            return downloadManager.openDownloadedFile(id)
        } else {
            null
        }
    }

    private fun openInstallSession(): Session {
        val params = SessionParams(MODE_FULL_INSTALL)
        params.setAppPackageName(APPLICATION_ID)
        val sessionId = packageInstaller.createSession(params)
        return packageInstaller.openSession(sessionId)
    }

    private fun onInstallResult(intent: Intent) {
        when (intent.getIntExtra(EXTRA_STATUS, -999)) {
            STATUS_PENDING_USER_ACTION -> {
                try {
                    val confirmationIntent: Intent? = intent.getParcelableExtra(EXTRA_INTENT)
                    confirmationIntent?.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                    Log.e(TAG, "Unable to start installation", e)
                    val broadcastIntent = MainActivity.installResultBroadcastIntent(null)
                    localBroadcastManager.sendBroadcast(broadcastIntent)
                }
            }
            else -> {
                val broadcastIntent = MainActivity.installResultBroadcastIntent(intent.extras)
                localBroadcastManager.sendBroadcast(broadcastIntent)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        executor.shutdown()
    }

}