package fr.groggy.racecontrol.tv.ui

import android.annotation.SuppressLint
import androidx.leanback.widget.DiffCallback
import arrow.optics.Getter

open class DataClassByIdDiffCallback<I, A> (val id: Getter<A, I>) : DiffCallback<A>() {

    override fun areItemsTheSame(oldItem: A, newItem: A): Boolean =
        id.get(oldItem) == id.get(newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: A, newItem: A): Boolean =
        oldItem == newItem

}