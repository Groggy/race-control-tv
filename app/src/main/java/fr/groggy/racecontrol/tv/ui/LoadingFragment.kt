package fr.groggy.racecontrol.tv.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import fr.groggy.racecontrol.tv.R

@Keep
@AndroidEntryPoint
class LoadingFragment : Fragment() {

    companion object {
        private val TAG = LoadingFragment::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

}