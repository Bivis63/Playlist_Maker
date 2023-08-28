package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.history.HistoryInteractor
import com.example.playlistmaker.player.domain.AudioPlayerIteractor
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerIteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {


    private val _stateLiveData = MutableLiveData<PlayerState>()
    val stateLiveData: LiveData<PlayerState> = _stateLiveData

    private val _favoriteLifeData = MutableLiveData<PlayerState.StateFavorite>()
    val favoriteLifeData: LiveData<PlayerState.StateFavorite> = _favoriteLifeData

    private var timeJob: Job? = null
    var isFavoriteTrack: Boolean = false

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

    }
}