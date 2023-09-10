package com.example.playlistmaker.media.data.db_for_playlists

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val imagePath: String,
    val trackIds: String,
    var trackCount: Int=0,
    var tracks: ArrayList<Long>,
    val imageUri: String?
)
