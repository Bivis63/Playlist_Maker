package com.example.playlistmaker.media.domain.db.playlists

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListsInteractor {

    suspend fun insertPlayList(playList: PlayListsModels)

    suspend fun getAllPlayLists(): Flow<List<PlayListsModels>>

    suspend fun updatePlayList(playList: PlayListsModels)

    suspend fun insertPlaylistTrack(playList: PlayListsModels, track: Track)

    fun saveImageToPrivateStorage(uri: Uri, context: Context)


}