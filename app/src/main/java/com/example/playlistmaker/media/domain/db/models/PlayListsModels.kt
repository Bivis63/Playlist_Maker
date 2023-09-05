package com.example.playlistmaker.media.domain.db.models



data class PlayListsModels(
    val id: Int,
    val name: String,
    val description: String,
    val imagePath: String,
    val trackIds: String,
    val trackCount: Int,
    val imageUri: String?
)
