package com.example.playlistmaker.presentation.audioPlayerPage

abstract class PlayerStateListener {
    abstract fun onPlayerPrepared()
    abstract fun onPlayerStarted()
    abstract fun onPlayerPause()
    abstract fun onPlayerStopped()
    abstract fun onPlayerCompletion()
    abstract fun onTrackDurationUpdate(currentPosition:Int)
}