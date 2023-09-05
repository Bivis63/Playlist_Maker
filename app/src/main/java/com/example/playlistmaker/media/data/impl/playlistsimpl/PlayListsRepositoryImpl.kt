package com.example.playlistmaker.media.data.impl.playlistsimpl

import com.example.playlistmaker.media.data.db_for_playlists.AppDatabasePlayLists
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.data.impl.converters.PlayListsConverter
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListsRepositoryImpl(
    private val appDatabasePlayLists: AppDatabasePlayLists,
    private val playListsConverter: PlayListsConverter
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

    private fun converterFromPlayListEntity(playList: List<PlaylistEntity>): List<PlayListsModels> {
        return playList.map { playLists -> playListsConverter.map(playLists) }
    }
}