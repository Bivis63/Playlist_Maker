package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.domain.db.HistoryInteractor
import com.example.playlistmaker.media.domain.db.HistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val repositoryOfSelectedTracks: HistoryRepository) :
    HistoryInteractor {

    override suspend fun getTracks(): Flow<List<Track>> {
        return repositoryOfSelectedTracks.getTracks()
    }

    override suspend fun insertTrack(track: Track) {
        return repositoryOfSelectedTracks.insertTrack(track = track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        return repositoryOfSelectedTracks.deleteTrack(trackId = trackId)
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> {
        return repositoryOfSelectedTracks.isFavoriteTrack(trackId)
    }

}