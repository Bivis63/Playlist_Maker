package com.example.playlistmaker.media.ui.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

open class NewPlayListViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {

    private val _stateLiveData = MutableStateFlow<NewPlayListsState>(NewPlayListsState.Empty)
    val stateLiveData: StateFlow<NewPlayListsState> = _stateLiveData

    private val _savedImageUri = MutableLiveData<Uri?>()
    val savedImageUri: LiveData<Uri?> = _savedImageUri


    fun insertPlayList(playLists: PlayListsModels) {
        viewModelScope.launch {
            playListsInteractor.insertPlayList(playLists)
        }
    }

    fun saveImageToPrivateStorage(uri: Uri, context: Context) {
        viewModelScope.launch {
            val savedUri = playListsInteractor.saveImageToPrivateStorage(uri, context)
            _savedImageUri.postValue(savedUri)
        }
    }

    fun generateImageTitle(): String {
        return "title_${UUID.randomUUID()}.jpg"
    }
}