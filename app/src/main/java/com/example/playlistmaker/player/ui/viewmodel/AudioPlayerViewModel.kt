package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.history.HistoryInteractor
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListsState
import com.example.playlistmaker.player.domain.AudioPlayerIteractor
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerIteractor,
    private val historyInteractor: HistoryInteractor,
    private val playListsInteractor: PlayListsInteractor
) : ViewModel() {


    private val _stateLiveData = MutableLiveData<PlayerState>()
    val stateLiveData: LiveData<PlayerState> = _stateLiveData

    private val _favoriteLifeData = MutableLiveData<PlayerState.StateFavorite>()
    val favoriteLifeData: LiveData<PlayerState.StateFavorite> = _favoriteLifeData

    private val _statePlayListsLiveData = MutableStateFlow<NewPlayListsState>(NewPlayListsState.Empty)
    val statePlayListsLiveData: StateFlow<NewPlayListsState> = _statePlayListsLiveData

    private var timeJob: Job? = null
    var isFavoriteTrack: Boolean = false
    private var isClickAllowed = true


    fun preparePlayer(songUrl: String) {
        renderState(PlayerState.Preparing)
        audioPlayerInteractor.preparePlayer(
            songUrl,
            onPrepared = { renderState(PlayerState.Stoped) },
            onCompletion = {
                renderState(PlayerState.Stoped)
                timeJob?.cancel()
            })
    }

    fun isInPlaylist(playlist: PlayListsModels, trackId: Long): Boolean{
        var result = false
        for(track in playlist.tracks) {
            if(track == trackId) result = true
        }
        return result
    }

    fun addToPlaylist(playlist: PlayListsModels, track: Track) {

        viewModelScope.launch {
            playlist.trackCount = playlist.tracks.size+1
            playListsInteractor.insertPlaylistTrack(playlist, track)
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun getAllPlayLists() {
        viewModelScope.launch(Dispatchers.IO) {
            playListsInteractor.getAllPlayLists().collect() { playLists ->
                if (playLists.isNotEmpty()) {
                    _statePlayListsLiveData.value = NewPlayListsState.NewPlayListsLoaded(playLists)
                } else {
                    _statePlayListsLiveData.value = NewPlayListsState.Empty
                }
            }
        }
    }

    fun playbackControl() {
        if (audioPlayerInteractor.isPlayerPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun pause() {
        pausePlayer()
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        setTrackDuration()
        renderState(PlayerState.Playing)

    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        renderState(PlayerState.Paused)
        timeJob?.cancel()
    }

    private fun setTrackDuration() {
        timeJob = viewModelScope.launch {
            while (true) {
                renderState(
                    PlayerState.PlayingTimeNow(
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        )
                            .format(
                                audioPlayerInteractor.getCurrentPosition()
                            )
                    )
                )
                delay(DELAY)
            }
        }
    }

    fun isFavorite(trackId: Int) {
        viewModelScope.launch {
            historyInteractor
                .isFavoriteTrack(trackId)
                .collect { isFavorite ->
                    isFavoriteTrack = isFavorite
                    _favoriteLifeData.postValue(PlayerState.StateFavorite(isFavorite))
                }
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (isFavoriteTrack) {
                historyInteractor.deleteTrack(track.trackId)
                _favoriteLifeData.postValue(PlayerState.StateFavorite(false))
                isFavoriteTrack = false
            } else {
                historyInteractor.insertTrack(track)
                _favoriteLifeData.postValue(PlayerState.StateFavorite(true))
                isFavoriteTrack = true
            }
        }
    }

    private fun renderState(state: PlayerState) {
        _stateLiveData.postValue(state)
    }

    companion object {
        private const val DELAY = 300L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}