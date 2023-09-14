package com.example.playlistmaker.media.data.db_for_playlists

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.media.data.db_for_playlists.dao.PlayListsDao
import com.example.playlistmaker.media.data.impl.converters.TracksConverter


@Database(version = 2, entities = [PlaylistEntity::class, PlaylistTrackEntity::class])
@TypeConverters(TracksConverter::class)
abstract class AppDatabasePlayLists :RoomDatabase() {
    abstract fun getPlayListsDao():PlayListsDao
}