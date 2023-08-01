package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.TrackHistory
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(
    private val trackHistory: TrackHistory,
    private val repository: TrackRepository
) : SearchInteractor {

    override fun clearHistory() {
        trackHistory.clearHistory()
    }

    override fun getHistory(): ArrayList<Track> {
        return trackHistory.getHistory()
    }

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result->
            when(result){
                is Resource.Success ->{
                    Pair(result.data,null)
                }
                is Resource.Error -> {
                    Pair(null,result.message)
                }
            }
        }
    }

    override fun addTrack(track: Track) {
        trackHistory.addTrack(track)
    }
}