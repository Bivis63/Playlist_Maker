package com.example.playlistmaker.media.data.db.dao

import androidx.room.*
import com.example.playlistmaker.media.data.db.TrackEntity


@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(tracks: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrackEntity(trackId: Int)

    @Query("SELECT * FROM track_table ORDER BY addingTime DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM track_table  WHERE trackId = :trackId)")
    suspend fun isFavoriteTrack(trackId: Int): Boolean
}