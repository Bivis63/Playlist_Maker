package com.example.playlistmaker.media.data.db_for_playlists.dao

import androidx.room.*
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayListsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList:PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getAllPlayLists(): List<PlaylistEntity>

    @Update
    suspend fun  updatePlayList(playList: PlaylistEntity)
}