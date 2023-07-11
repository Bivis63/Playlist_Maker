package com.example.playlistmaker.di

import com.example.playlistmaker.media.ui.viewModel.PlayListsViewModel
import com.example.playlistmaker.media.ui.viewModel.SelectedTracksViewModel
import com.example.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(audioPlayerInteractor = get())

    }

    viewModel {
        SearchViewModel(searchInteractor = get())
    }

    viewModel {
        SettingViewModel(sharingInteractor = get(), themeUseCase = get())
    }

    viewModel {
        SelectedTracksViewModel()
    }

    viewModel {
        PlayListsViewModel()
    }
}
