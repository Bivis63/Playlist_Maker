package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow


interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}