package fr.groggy.racecontrol.tv.ui.session

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId

@AndroidEntryPoint
class SessionBrowseActivity : FragmentActivity() {

    companion object {
        private val TAG = SessionBrowseActivity::class.simpleName

        private const val SESSION_ID = "sessionId"

        fun intent(activity: Activity, sessionId: F1TvSessionId): Intent {
            val intent = Intent(activity.baseContext, SessionBrowseActivity::class.java)
            intent.putExtra(SESSION_ID, sessionId.value)
            return intent
        }

        fun getSessionId(activity: Activity): F1TvSessionId =
            F1TvSessionId(activity.intent.getStringExtra(SESSION_ID)!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_browse)
    }

}