package com.example.playlistmaker.player.domain

interface AudioPlayer {
    fun start()
    fun pause()
    fun isPlayerPlaying(): Boolean
    fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun getCurrentPosition(): Int
}