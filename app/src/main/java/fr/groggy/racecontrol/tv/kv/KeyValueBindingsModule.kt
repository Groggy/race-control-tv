package fr.groggy.racecontrol.tv.kv

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import fr.groggy.racecontrol.tv.core.season.CurrentSeasonIdRepository
import fr.groggy.racecontrol.tv.core.token.F1TokenRepository
import fr.groggy.racecontrol.tv.core.token.F1TvTokenRepository
import fr.groggy.racecontrol.tv.kv.season.SharedPreferencesCurrentSeasonIdRepository
import fr.groggy.racecontrol.tv.kv.token.SharedPreferencesF1TokenRepository
import fr.groggy.racecontrol.tv.kv.token.SharedPreferencesF1TvTokenRepository

@Module
@InstallIn(ApplicationComponent::class)
abstract class KeyValueBindingsModule {

    @Binds
    abstract fun currentSeasonIdRepository(repository: SharedPreferencesCurrentSeasonIdRepository): CurrentSeasonIdRepository

    @Binds
    abstract fun f1TokenRepository(repository: SharedPreferencesF1TokenRepository): F1TokenRepository

    @Binds
    abstract fun f1TvTokenRepository(repository: SharedPreferencesF1TvTokenRepository): F1TvTokenRepository

}