package com.example.playlistmaker.media.data.impl.playlistsimpl

import com.example.playlistmaker.media.data.db_for_playlists.AppDatabasePlayLists
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.playlists.PlayListsRepository

class PlayListsRepositoryImpl(private val appDatabasePlayLists: AppDatabasePlayLists):PlayListsRepository {
    override suspend fun insertPlayList(playList: PlaylistEntity) {
        appDatabasePlayLists.getPlayListsDao().insertPlayList(playList)
    }

    override suspend fun updatePlayList(playList: PlaylistEntity) {
        appDatabasePlayLists.getPlayListsDao().updatePlayList(playList)
    }
}