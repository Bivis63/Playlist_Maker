package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.AudioPlayer

class AudioPlayerImpl() : AudioPlayer {

    private var mediaPlayer = MediaPlayer()
    private var isPrepared = false


    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        if (isPrepared) return
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                isPrepared = true
                onPrepared.invoke()
            }
            setOnCompletionListener {
                onCompletion.invoke()
            }
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        if (isPlayerPlaying()) {
            mediaPlayer.pause()
        }
    }

    override fun isPlayerPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }


}