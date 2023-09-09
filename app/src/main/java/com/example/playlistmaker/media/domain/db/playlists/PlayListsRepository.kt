package com.example.playlistmaker.media.domain.db.playlists

import com.example.playlistmaker.media.data.db_for_playlists.PlaylistTrackEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListsRepository {

    suspend fun insertPlayList(playList: PlayListsModels)

    suspend fun getAllPlayLists(): Flow<List<PlayListsModels>>

    suspend fun updatePlayList(playList: PlayListsModels)

    suspend fun insertPlaylistTrack(playList: PlayListsModels, track: Track)


}