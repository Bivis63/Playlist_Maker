package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.AudioPlayer
import com.example.playlistmaker.player.domain.AudioPlayerIteractor

class AudioPlayerIteractorImpl(private val audioPlayer: AudioPlayer):AudioPlayerIteractor {
    override fun startPlayer() {
        audioPlayer.start()
    }

    override fun pausePlayer() {
        audioPlayer.pause()
    }


    override fun isPlayerPlaying(): Boolean {
        return audioPlayer.isPlayerPlaying()
    }

    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        audioPlayer.preparePlayer(url,onPrepared,onCompletion)
    }


    override fun getCurrentPosition(): Int {
        return audioPlayer.getCurrentPosition()
    }
}