package com.example.playlistmaker.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.presentation.audioPlayerPage.AudioPlayerInteractor
import com.example.playlistmaker.presentation.audioPlayerPage.PlayerStateListener
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerInteractorImpl : AudioPlayerInteractor {

    lateinit var binding: ActivityAudioPlayerBinding
    var playerState = STATE_DEFAULT
    val duration = setTrackDuration()
    var mainThreadHandler: Handler? = null
    var mediaPlayer = MediaPlayer()

    private var playerStateListener: PlayerStateListener? = null

    fun setPlayerStateListener(listener: PlayerStateListener){
        this.playerStateListener = listener
    }

    override fun preparePlayer(songUrl: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(songUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
            playerStateListener?.onPlayerPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            mainThreadHandler?.removeCallbacks(duration)
            playerState = STATE_PREPARED
            setIcon()
            binding.trackTimeNow.text = "00:00"
            playerStateListener?.onPlayerCompletion()


        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler?.post(duration)
        playerStateListener?.onPlayerStarted()

    }

    override fun pauseTrack() {
        mainThreadHandler?.removeCallbacks(duration)
        mediaPlayer.pause()
        setIcon()
        playerState = STATE_PAUSED
        playerStateListener?.onPlayerPause()
    }

    override fun playBackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pauseTrack()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                setIcon()
                startPlayer()
            }
        }
    }

    override fun setTrackDuration(): Runnable {
        mainThreadHandler = Handler(Looper.getMainLooper())
        return object : Runnable {
            override fun run() {
                binding.trackTimeNow.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                mainThreadHandler?.postDelayed(this, DELAY)
                playerStateListener?.onTrackDurationUpdate(mediaPlayer.currentPosition)

            }
        }
    }

    fun setIcon() {
        when (playerState) {
            STATE_PLAYING -> binding.playButton.setImageResource(R.drawable.baseline_pause_24)
            STATE_PAUSED -> binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            else -> binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
    }
}