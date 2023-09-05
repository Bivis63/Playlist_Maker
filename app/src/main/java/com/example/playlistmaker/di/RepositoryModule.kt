package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.impl.converters.PlayListsConverter
import com.example.playlistmaker.media.data.impl.converters.TrackDbConverter
import com.example.playlistmaker.media.data.impl.historyimpl.HistoryRepositoryImpl
import com.example.playlistmaker.media.data.impl.playlistsimpl.PlayListsRepositoryImpl
import com.example.playlistmaker.media.domain.db.history.HistoryRepository
import com.example.playlistmaker.media.domain.db.playlists.PlayListsRepository
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.settings.data.SharedPreferencesThemeRepository
import com.example.playlistmaker.settings.data.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }
    single<ThemeRepository> {
        SharedPreferencesThemeRepository(get())
    }
    factory { TrackDbConverter() }

    factory { PlayListsConverter() }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }
    single<PlayListsRepository> {
        PlayListsRepositoryImpl(get(), get())
    }
}