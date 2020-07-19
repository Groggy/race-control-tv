package fr.groggy.racecontrol.tv.core

import java.time.Instant
import java.time.LocalDate

data class LocalDatePeriod(val start: LocalDate, val end: LocalDate)

data class InstantPeriod(val start: Instant, val end: Instant)