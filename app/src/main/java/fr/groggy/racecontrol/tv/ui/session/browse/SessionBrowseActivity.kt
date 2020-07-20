package fr.groggy.racecontrol.tv.ui.session.browse

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

        fun intent(activity: Activity, sessionId: F1TvSessionId): Intent {
            val intent = Intent(activity.baseContext, SessionBrowseActivity::class.java)
            SessionGridFragment.putSessionId(intent, sessionId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_browse)
    }

}