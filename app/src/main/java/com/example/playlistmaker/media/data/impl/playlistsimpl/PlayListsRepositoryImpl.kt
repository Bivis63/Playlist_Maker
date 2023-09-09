package com.example.playlistmaker.media.data.impl.playlistsimpl

import com.example.playlistmaker.media.data.db_for_playlists.AppDatabasePlayLists
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistTrackEntity
import com.example.playlistmaker.media.data.impl.converters.PlayListsConverter
import com.example.playlistmaker.media.data.impl.converters.PlaylistTrackConverter
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListsRepositoryImpl(
    private val appDatabasePlayLists: AppDatabasePlayLists,
    private val playListsConverter: PlayListsConverter,
    private val playlistTrackConverter:PlaylistTrackConverter
) : PlayListsRepository {
    override suspend fun insertPlayList(playList: PlayListsModels) {
        appDatabasePlayLists.getPlayListsDao().insertPlayList(playListsConverter.map(playList))
    }

    override suspend fun getAllPlayLists(): Flow<List<PlayListsModels>> = flow{
       val playLists= appDatabasePlayLists.getPlayListsDao().getAllPlayLists()
        emit(converterFromPlayListEntity(playLists))
    }

    override suspend fun updatePlayList(playList: PlayListsModels) {
        appDatabasePlayLists.getPlayListsDao().updatePlayList(playListsConverter.map(playList))
    }

    override suspend fun insertPlaylistTrack(playList: PlayListsModels,track: Track) {
        playList.tracks.add(track.trackId.toLong())
        insertPlayList(playList)
        appDatabasePlayLists.getPlayListsDao().insertPlaylistTrack(playlistTrackConverter.map(track))
    }

    private fun converterFromPlayListEntity(playList: List<PlaylistEntity>): List<PlayListsModels> {
        return playList.map { playLists -> playListsConverter.map(playLists) }
    }

    private fun converterFromPlaylistsTrackEntity(track: List<PlaylistTrackEntity>):List<Track>{
        return track.map { track ->playlistTrackConverter.map(track) }
    }
}