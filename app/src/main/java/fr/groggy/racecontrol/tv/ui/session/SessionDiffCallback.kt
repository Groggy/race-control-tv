package fr.groggy.racecontrol.tv.ui.session

import fr.groggy.racecontrol.tv.core.Session
import fr.groggy.racecontrol.tv.core.id
import fr.groggy.racecontrol.tv.f1tv.F1TvSessionId
import fr.groggy.racecontrol.tv.ui.DataClassByIdDiffCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDiffCallback @Inject constructor() : DataClassByIdDiffCallback<F1TvSessionId, Session>(Session.id.asGetter())