package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.settings.data.SharedPreferencesThemeRepository
import com.example.playlistmaker.settings.data.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository>{
        TrackRepositoryImpl(get())
    }
    single<ThemeRepository>{
        SharedPreferencesThemeRepository(get())
    }
}