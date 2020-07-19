package fr.groggy.racecontrol.tv.ui.channel

import fr.groggy.racecontrol.tv.core.channel.Channel
import fr.groggy.racecontrol.tv.f1tv.F1TvChannelId
import fr.groggy.racecontrol.tv.ui.DataClassByIdDiffCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelDiffCallback @Inject constructor() : DataClassByIdDiffCallback<F1TvChannelId, Channel>(
    Channel.id)