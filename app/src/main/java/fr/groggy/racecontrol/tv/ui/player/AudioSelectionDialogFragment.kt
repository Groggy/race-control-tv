package fr.groggy.racecontrol.tv.ui.player

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.TrackGroupArray
import fr.groggy.racecontrol.tv.R

class AudioSelectionDialogFragment(
    trackGroupArray: TrackGroupArray
) : DialogFragment() {

    private var onAudioLanguageSelectedListener: ((String?) -> Unit)? = null

    private val formats by lazy {
        val formats = mutableListOf<Format>()
        for (i in 0 until trackGroupArray.length) {
            val trackGroup = trackGroupArray[i]
            for (j in 0 until trackGroup.length) {
                formats.add(trackGroup.getFormat(j))
            }
        }
        formats.toList()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = formats.map { it.label }.toTypedArray()
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.audio_selection_dialog_title)
            .setItems(items) { _, i -> selectAudio(i) }
            .create()
    }

    private fun selectAudio(index: Int) {
        val format = formats[index]
        onAudioLanguageSelectedListener?.let { it(format.language) }
    }

    fun onAudioLanguageSelected(listener: (String?) -> Unit) {
        onAudioLanguageSelectedListener = listener
    }

}