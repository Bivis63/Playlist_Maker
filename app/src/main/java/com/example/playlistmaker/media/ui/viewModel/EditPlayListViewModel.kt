package com.example.playlistmaker.media.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import kotlinx.coroutines.launch

class EditPlayListViewModel(private val playListsInteractor: PlayListsInteractor) : NewPlayListViewModel(playListsInteractor) {

    private val _editedPlayList = MutableLiveData<PlayListsModels>()
    val editedPlayList: LiveData<PlayListsModels> get() = _editedPlayList

    fun setEditedPlayList(playList: PlayListsModels) {
        _editedPlayList.postValue(playList)
    }

    fun updatePlayList(playLists: PlayListsModels) {
        viewModelScope.launch {
            playListsInteractor.updatePlayList(playLists)
        }
    }
}
