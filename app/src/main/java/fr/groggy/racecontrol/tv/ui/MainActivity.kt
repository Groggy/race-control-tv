package fr.groggy.racecontrol.tv.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_TASK_ON_HOME
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.core.credentials.CredentialsService
import fr.groggy.racecontrol.tv.ui.season.browse.SeasonBrowseActivity
import fr.groggy.racecontrol.tv.ui.signin.SignInActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    @Inject lateinit var credentialsService: CredentialsService

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            val intent = if (credentialsService.hasValidF1Credentials()) {
                SeasonBrowseActivity.intent(this@MainActivity)
            } else {
                SignInActivity.intent(this@MainActivity)
            }
            startActivity(intent)
            finish()
        }
    }

}