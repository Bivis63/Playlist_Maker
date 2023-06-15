package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface TrackHistory {
    fun addTrack (track: Track)
    fun clearHistory()
    fun getHistory():ArrayList<Track>
}