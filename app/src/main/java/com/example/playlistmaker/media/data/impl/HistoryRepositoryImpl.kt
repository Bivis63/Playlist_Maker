package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.impl.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.db.HistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDataBase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : HistoryRepository {

    override suspend fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getTracks()
        emit(converterFromTrackEntity(tracks))
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> = flow {
        val isFavorite = appDataBase.trackDao().isFavoriteTrack(trackId)
        emit(isFavorite)
    }

    override suspend fun insertTrack(track: Track) {
        appDataBase.trackDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        appDataBase.trackDao().deleteTrackEntity(trackId)
    }

    private fun converterFromTrackEntity(track: List<TrackEntity>): List<Track> {
        return track.map { track -> trackDbConverter.map(track) }
    }
}