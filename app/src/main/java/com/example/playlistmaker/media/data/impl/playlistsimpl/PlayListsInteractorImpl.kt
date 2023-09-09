package com.example.playlistmaker.media.data.impl.playlistsimpl


import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsInteractor
import com.example.playlistmaker.media.domain.db.playlists.PlayListsRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListsInteractorImpl(private val playListsRepository: PlayListsRepository):PlayListsInteractor {
    override suspend fun insertPlayList(playList: PlayListsModels) {
        playListsRepository.insertPlayList(playList)
    }

    override suspend fun getAllPlayLists(): Flow<List<PlayListsModels>> {
      return  playListsRepository.getAllPlayLists()
    }

    override suspend fun updatePlayList(playList: PlayListsModels) {
        playListsRepository.updatePlayList(playList)
    }

    override suspend fun insertPlaylistTrack(playList: PlayListsModels, track: Track) {
        playListsRepository.insertPlaylistTrack(playList,track)
    }
}