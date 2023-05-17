package com.example.playlistmaker.presentation.audioPlayerPage

interface AudioPlayerInteractor {
    fun startPlayer()
    fun preparePlayer(songUrl: String)
    fun pauseTrack()
    fun playBackControl()
    fun setTrackDuration(): Runnable
}