package com.example.playlistmaker.media.domain.db.history


import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    suspend fun getTracks(): Flow<List<Track>>

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean>
}