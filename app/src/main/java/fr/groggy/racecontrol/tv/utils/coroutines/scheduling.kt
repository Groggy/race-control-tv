package fr.groggy.racecontrol.tv.utils.coroutines

import kotlinx.coroutines.delay
import kotlin.time.Duration

suspend fun schedule(duration: Duration, f: suspend () -> Any) {
    while (true) {
        f()
        delay(duration)
    }
}