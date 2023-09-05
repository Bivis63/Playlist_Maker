package com.example.playlistmaker.media.domain.db.playlists

import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import kotlinx.coroutines.flow.Flow

interface PlayListsInteractor {

    suspend fun insertPlayList(playList: PlayListsModels)

    suspend fun getAllPlayLists(): Flow<List<PlayListsModels>>

    suspend fun  updatePlayList(playList: PlayListsModels)
}