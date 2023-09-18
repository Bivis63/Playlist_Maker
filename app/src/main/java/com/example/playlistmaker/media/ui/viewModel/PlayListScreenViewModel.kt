package com.example.playlistmaker.media.ui.viewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max


class PlayListScreenViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {

    private lateinit var tracks: List<Track>

    private val _duration = MutableLiveData<Long>()
    fun observeDurationState(): LiveData<Long> = _duration

    private val _playlist = MutableLiveData<PlayListsModels>()
    fun observeState(): LiveData<PlayListsModels> = _playlist

    private val _playlistTracks = MutableLiveData<List<Track>>()
    fun observePlaylistTrackState(): LiveData<List<Track>> = _playlistTracks

    private val _playlistTrackCount = MutableLiveData<Int>()
    fun observePlaylistTrackCount(): LiveData<Int> = _playlistTrackCount

    fun getDurationAllTracks() {
        if (_playlistTracks.value != null) {
            var duration: Long = 0
            _playlistTracks.value!!.forEach {
                duration += it.trackTimeMillis
            }
            _duration.postValue(duration)
        }
    }

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch {
            _playlist.postValue(playListsInteractor.getPlaylistById(playlistId))

        }
    }

    fun getAllPlaylistTracks(playlistId: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            tracks = playListsInteractor.getAllPlaylistTracks(playlistId)
            _playlistTracks.postValue(tracks)
        }
    }


    fun deleteTrackFromPlaylist(trackId: Long) {
        val playlistId = _playlist.value?.id
        viewModelScope.launch(Dispatchers.IO) {
            if (playlistId != null) {
                playListsInteractor.deleteTrackFromPlaylist(playlistId, trackId)
                decrementPlaylistTrackCount(playlistId)
                getPlaylistTracksAfterDeletingTrack(playlistId)
            }
        }
    }

    private fun getPlaylistTracksAfterDeletingTrack(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedPlaylist = playListsInteractor.getPlaylistById(playlistId)
            _playlist.postValue(updatedPlaylist)
            val updatedTracks = playListsInteractor.getAllPlaylistTracks(updatedPlaylist.tracks)
            _playlistTracks.postValue(updatedTracks)
            _playlistTrackCount.postValue(updatedTracks.size)
        }
    }

    private suspend fun decrementPlaylistTrackCount(playlistId: Int) {
        try {
            playListsInteractor.decrementPlaylistTrackCount(playlistId)
        } catch (e: Exception) {

        }
    }


}
