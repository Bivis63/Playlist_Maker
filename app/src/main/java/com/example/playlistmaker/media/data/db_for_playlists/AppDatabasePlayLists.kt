package com.example.playlistmaker.media.data.db_for_playlists

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.db_for_playlists.dao.PlayListsDao


@Database(version = 1, entities = [PlaylistEntity::class])
abstract class AppDatabasePlayLists :RoomDatabase() {
    abstract fun getPlayListsDao():PlayListsDao
}