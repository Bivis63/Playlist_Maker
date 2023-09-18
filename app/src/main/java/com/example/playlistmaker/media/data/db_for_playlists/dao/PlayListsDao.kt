package com.example.playlistmaker.media.data.db_for_playlists.dao

import androidx.room.*
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayListsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList:PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getAllPlayLists(): List<PlaylistEntity>

    @Update
    suspend fun  updatePlayList(playList: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("SELECT * FROM playlist_tracks")
    fun getAllPlaylistTracks(): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("UPDATE playlists SET trackCount = trackCount - 1 WHERE id = :playlistId")
    suspend fun decrementPlaylistTrackCount(playlistId: Int)
}