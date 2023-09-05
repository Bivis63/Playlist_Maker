package com.example.playlistmaker.media.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class NewPlayListViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {

    private val _stateLiveData = MutableStateFlow<NewPlayListsState>(NewPlayListsState.Empty)
    val stateLiveData: StateFlow<NewPlayListsState> = _stateLiveData


    fun insertPlayList(playLists: PlayListsModels) {
        viewModelScope.launch {
            playListsInteractor.insertPlayList(playLists)
        }
    }


    fun generateImageTitle(): String {
        return "title_${UUID.randomUUID()}.jpg"
    }
}