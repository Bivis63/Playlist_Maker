package com.example.playlistmaker.media.data.impl.playlistsimpl


import android.content.Context
import android.net.Uri
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
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

    override  fun saveImageToPrivateStorage(uri: Uri, context: Context):Uri? {
       return playListsRepository.saveImageToPrivateStorage(uri, context)
    }

    override suspend fun getPlaylistById(playlistId: Int): PlayListsModels {
        return playListsRepository.getPlaylistById(playlistId)
    }

   override suspend fun getAllPlaylistTracks(playlistId: List<Long>): List<Track> {
        return playListsRepository.getAllPlaylistTracks(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playListId: Int, trackId: Long) {
        return playListsRepository.deleteTrackFromPlaylist(playListId,trackId)
    }

    override suspend fun decrementPlaylistTrackCount(playlistId: Int) {
        return playListsRepository.decrementPlaylistTrackCount(playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        return playListsRepository.deletePlaylist(playlistId)
    }

}