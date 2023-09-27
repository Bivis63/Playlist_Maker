package com.example.playlistmaker.media.domain.db.models

import java.io.Serializable


data class PlayListsModels(
    val id: Int,
    val name: String,
    val description: String,
    val imagePath: String,
    val trackIds: String,
    var trackCount: Int,
    var tracks: ArrayList<Long>,
    val imageUri: String?
): Serializable
