package com.example.playlistmaker.player.domain

interface AudioPlayerIteractor {
    fun startPlayer()
    fun pausePlayer()
    fun isPlayerPlaying(): Boolean
    fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun getCurrentPosition(): Int
}