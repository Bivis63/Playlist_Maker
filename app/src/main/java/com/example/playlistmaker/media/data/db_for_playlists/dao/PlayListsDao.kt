package com.example.playlistmaker.media.data.db_for_playlists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity


@Dao
interface PlayListsDao {

    @Insert
    suspend fun insertPlayList(playList:PlaylistEntity)

    @Update
    suspend fun  updatePlayList(playList: PlaylistEntity)
}