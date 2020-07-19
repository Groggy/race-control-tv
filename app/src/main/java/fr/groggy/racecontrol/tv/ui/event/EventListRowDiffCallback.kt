package fr.groggy.racecontrol.tv.ui.event

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.DiffCallback
import androidx.leanback.widget.ListRow
import fr.groggy.racecontrol.tv.core.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventListRowDiffCallback @Inject constructor() : DiffCallback<ListRow>() {

    override fun areItemsTheSame(oldItem: ListRow, newItem: ListRow): Boolean =
        oldItem.headerItem.name == newItem.headerItem.name

    override fun areContentsTheSame(oldItem: ListRow, newItem: ListRow): Boolean {
        val oldContent = (oldItem.adapter as ArrayObjectAdapter).unmodifiableList<Session>()
        val newContent = (newItem.adapter as ArrayObjectAdapter).unmodifiableList<Session>()
        return oldContent == newContent
    }

}