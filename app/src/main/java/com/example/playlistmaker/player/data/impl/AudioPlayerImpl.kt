package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.AudioPlayer

class AudioPlayerImpl() : AudioPlayer {

    private var mediaPlayer =  MediaPlayer()


    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
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