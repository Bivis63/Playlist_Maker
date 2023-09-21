package com.example.playlistmaker.search.data.impl


import com.example.playlistmaker.search.data.NetworkClient

import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(response.resultCode.toString()))
            }
            200 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName ?: "",
                            it.artistName ?: "",
                            it.trackTimeMillis,
                            it.artworkUrl100 ?: "",
                            it.collectionName ?: "",
                            it.releaseDate ?: "",
                            it.primaryGenreName ?: "",
                            it.country ?: "",
                            it.previewUrl ?: "",
                            it.addingTime,
                            it.artworkUrl60 ?:""
                        )
                    }

                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(response.resultCode.toString()))
            }
        }
    }
}