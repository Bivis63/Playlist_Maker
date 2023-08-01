package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun addTrack(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

}