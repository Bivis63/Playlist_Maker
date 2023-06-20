package com.example.playlistmaker.player.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.AudioPlayerIteractor
import com.example.playlistmaker.player.ui.PlayerState
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerIteractor) : ViewModel() {


    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private val duration = setTrackDuration()


    private val _stateLiveData = MutableLiveData<PlayerState>()
    val stateLiveData: LiveData<PlayerState> = _stateLiveData


    fun preparePlayer(songUrl: String) {
        renderState(PlayerState.Preparing)
        audioPlayerInteractor.preparePlayer(
            songUrl,
            onPrepared = { renderState(PlayerState.Stoped) },
            onCompletion = {
                renderState(PlayerState.Stoped)
                mainThreadHandler?.removeCallbacks(duration)
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
        renderState(PlayerState.Playing)
        mainThreadHandler?.post(duration)

    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        renderState(PlayerState.Paused)
        mainThreadHandler?.removeCallbacks(duration)
    }


    private fun setTrackDuration(): Runnable {
        return object : Runnable {
            override fun run() {
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
                mainThreadHandler?.postDelayed(this, DELAY)

            }
        }
    }

    private fun renderState(state: PlayerState) {
        _stateLiveData.postValue(state)
    }

    companion object {
        private const val DELAY = 300L

        fun getAudioPlayerViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val audioPlayerIteractor = Creator.providePlayerInteractor()
                    return AudioPlayerViewModel(audioPlayerIteractor) as T
                }
            }


    }
}