package fr.groggy.racecontrol.tv.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import fr.groggy.racecontrol.tv.R
import fr.groggy.racecontrol.tv.core.release.Release
import io.noties.markwon.Markwon

class ReleaseUpdateDialogFragment(
    private val release: Release,
    private val onUpdate: () -> Unit,
    private val onSkip: () -> Unit,
    private val onCancel: () -> Unit,
    private val markwon: Markwon
) : DialogFragment() {

    companion object {
        private val TAG = ReleaseUpdateDialogFragment::class.simpleName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog")
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.update_version_dialog_title, release.version.stringValue))
            .setMessage(markwon.toMarkdown(release.description.replace("#", "##")))
            .setPositiveButton(R.string.update) { _, _ -> onUpdate() }
            .setNeutralButton(R.string.skip) { _, _ -> onSkip() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        Log.d(TAG, "onCancel")
        onCancel()
    }

}