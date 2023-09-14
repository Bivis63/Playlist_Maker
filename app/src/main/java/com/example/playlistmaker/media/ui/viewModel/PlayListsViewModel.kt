package com.example.playlistmaker.media.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayListsViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {

    private val _stateLiveData = MutableStateFlow<NewPlayListsState>(NewPlayListsState.Empty)
    val stateLiveData: StateFlow<NewPlayListsState> = _stateLiveData
    fun getAllPlayLists() {
        viewModelScope.launch(Dispatchers.IO) {
            playListsInteractor.getAllPlayLists().collect() { playLists ->
                if (playLists.isNotEmpty()) {
                    _stateLiveData.value = NewPlayListsState.NewPlayListsLoaded(playLists)
                } else {
                    _stateLiveData.value = NewPlayListsState.Empty
                }
            }
        }
    }
}