package com.example.playlistmaker.media.data.impl.playlistsimpl

import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.media.domain.db.playlists.PlayListsRepository

class PlayListsInteractorImpl(private val playListsRepository: PlayListsRepository):PlayListsInteractor {
    override suspend fun insertPlayList(playList: PlaylistEntity) {
        playListsRepository.insertPlayList(playList)
    }

    override suspend fun updatePlayList(playList: PlaylistEntity) {
        playListsRepository.updatePlayList(playList)
    }
}