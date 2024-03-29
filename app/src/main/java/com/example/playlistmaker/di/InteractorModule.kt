package com.example.playlistmaker.di


import com.example.playlistmaker.media.data.impl.historyimpl.HistoryInteractorImpl
import com.example.playlistmaker.media.data.impl.playlistsimpl.PlayListsInteractorImpl
import com.example.playlistmaker.media.domain.db.history.HistoryInteractor
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.player.domain.AudioPlayerIteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerIteractorImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.settings.domain.usecase.ThemeUseCase
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<AudioPlayerIteractor> {
        AudioPlayerIteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get(), get())

    }

    single {
        ThemeUseCase(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
    single <PlayListsInteractor>{
        PlayListsInteractorImpl(get())
    }

}