package com.example.playlistmaker.di

import com.example.playlistmaker.media.ui.viewModel.*
import com.example.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(audioPlayerInteractor = get(), historyInteractor =  get(), playListsInteractor = get())

    }

    viewModel {
        SearchViewModel(searchInteractor = get())
    }

    viewModel {
        SettingViewModel(sharingInteractor = get(), themeUseCase = get())
    }

    viewModel {
        SelectedTracksViewModel(get())
    }

    viewModel {
        PlayListsViewModel(get())
    }
    viewModel {
        NewPlayListViewModel(playListsInteractor = get())
    }
    viewModel{
        PlayListScreenViewModel(playListsInteractor = get())
    }

    viewModel { EditPlayListViewModel(playListsInteractor = get()) }
}
