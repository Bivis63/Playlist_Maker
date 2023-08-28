package com.example.playlistmaker.media.domain.db.playlists

import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity

interface PlayListsInteractor {

    suspend fun insertPlayList(playList: PlaylistEntity)

    suspend fun  updatePlayList(playList: PlaylistEntity)
}