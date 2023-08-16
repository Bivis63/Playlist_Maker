package com.example.playlistmaker.player.ui

sealed class PlayerState {
    data class PlayingTimeNow(
        val playingTime: String
    ) : PlayerState()

    object Preparing : PlayerState()
    object Paused : PlayerState()
    object Playing : PlayerState()
    object Stoped : PlayerState()

    data class StateFavorite(
        val isFavorite: Boolean
    ) : PlayerState()
}