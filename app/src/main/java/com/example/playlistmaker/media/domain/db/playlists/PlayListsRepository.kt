package com.example.playlistmaker.media.domain.db.playlists

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistTrackEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListsRepository {

    suspend fun insertPlayList(playList: PlayListsModels)

    suspend fun getAllPlayLists(): Flow<List<PlayListsModels>>

    suspend fun updatePlayList(playList: PlayListsModels)

    suspend fun insertPlaylistTrack(playList: PlayListsModels, track: Track)

    fun saveImageToPrivateStorage(uri: Uri, context: Context):Uri?

    suspend fun getPlaylistById(playlistId: Int): PlayListsModels

    suspend fun getAllPlaylistTracks(trackIdList: List<Long>): List<Track>

    suspend fun deleteTrackFromPlaylist(playListId: Int, trackId: Long)

    suspend fun decrementPlaylistTrackCount(playlistId: Int)

    suspend fun deletePlaylist(playlistId: Int)

}