package com.example.playlistmaker.media.data.impl.converters

import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels

class PlayListsConverter {

    fun map(playListsModels: PlayListsModels): PlaylistEntity {
        return PlaylistEntity(
            playListsModels.id,
            playListsModels.name,
            playListsModels.description,
            playListsModels.imagePath,
            playListsModels.trackIds,
            playListsModels.trackCount,
            playListsModels.imageUri
        )
    }

    fun map(playLists: PlaylistEntity): PlayListsModels {
        return PlayListsModels(
            playLists.id,
            playLists.name,
            playLists.description,
            playLists.imagePath,
            playLists.trackIds,
            playLists.trackCount,
            playLists.imageUri
        )
    }
}