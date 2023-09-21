package com.example.playlistmaker.media.data.impl.playlistsimpl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.media.data.db_for_playlists.AppDatabasePlayLists
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistTrackEntity
import com.example.playlistmaker.media.data.impl.converters.PlayListsConverter
import com.example.playlistmaker.media.data.impl.converters.PlaylistTrackConverter
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.domain.db.playlists.PlayListsRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream

class PlayListsRepositoryImpl(
    private val appDatabasePlayLists: AppDatabasePlayLists,
    private val playListsConverter: PlayListsConverter,
    private val playlistTrackConverter: PlaylistTrackConverter
) : PlayListsRepository {
    override suspend fun insertPlayList(playList: PlayListsModels) {
        appDatabasePlayLists.getPlayListsDao().insertPlayList(playListsConverter.map(playList))
    }

    override suspend fun getAllPlayLists(): Flow<List<PlayListsModels>> = flow {
        val playLists = appDatabasePlayLists.getPlayListsDao().getAllPlayLists()
        emit(converterFromPlayListEntity(playLists))
    }

    override suspend fun updatePlayList(playList: PlayListsModels) {
        appDatabasePlayLists.getPlayListsDao().updatePlayList(playListsConverter.map(playList))
    }

    override suspend fun insertPlaylistTrack(playList: PlayListsModels, track: Track) {
        playList.tracks.add(track.trackId.toLong())
        insertPlayList(playList)
        appDatabasePlayLists.getPlayListsDao()
            .insertPlaylistTrack(playlistTrackConverter.map(track))
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri, context: Context): Uri? {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "cover_${System.currentTimeMillis()}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        val uri = Uri.fromFile(file)
        return uri
    }

    override suspend fun getPlaylistById(playlistId: Int): PlayListsModels {
        return convertFromPlaylistEntity(
            appDatabasePlayLists.getPlayListsDao().getPlaylistById(playlistId)
        )
    }

    override suspend fun getAllPlaylistTracks(playlistIds: List<Long>): List<Track> {
        val playlist = appDatabasePlayLists.getPlayListsDao().getAllPlaylistTracks()
        return playlist
            .filter { it.trackId.toLong() in playlistIds }
            .map { convertFromTrackEntity(it) }
    }

    override suspend fun deleteTrackFromPlaylist(playListId: Int, trackId: Long) {
        val playlist = getPlaylistById(playListId)
        playlist.tracks.remove(trackId)
        updatePlayList(playlist)

        if (!isTrackInAnyPlaylist(trackId)) {
            deleteTrackIfNotInAnyPlaylist(trackId)
        }
    }

    override suspend fun decrementPlaylistTrackCount(playlistId: Int) {
        appDatabasePlayLists.getPlayListsDao().decrementPlaylistTrackCount(playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlist = getPlaylistById(playlistId)
        appDatabasePlayLists.getPlayListsDao().deletePlaylist(playListsConverter.map(playlist))
    }

    private fun converterFromPlayListEntity(playList: List<PlaylistEntity>): List<PlayListsModels> {
        return playList.map { playLists -> playListsConverter.map(playLists) }
    }

    private fun converterFromPlaylistsTrackEntity(track: List<PlaylistTrackEntity>): List<Track> {
        return track.map { track -> playlistTrackConverter.map(track) }
    }

    private fun convertFromPlaylistEntity(playList: PlaylistEntity): PlayListsModels {
        return playListsConverter.map(playList)
    }

    private fun convertFromTrackEntity(trackEntity: PlaylistTrackEntity): Track {
        return playlistTrackConverter.map(trackEntity)
    }

    private fun isTrackInAnyPlaylist(trackId: Long): Boolean {
        val allPlaylists = appDatabasePlayLists.getPlayListsDao().getAllPlayLists()

        for (playlist in allPlaylists) {
            if (trackId in playlist.tracks) {
                return true
            }
        }
        return false
    }

    private suspend fun deleteTrackIfNotInAnyPlaylist(trackId: Long) {
        appDatabasePlayLists.getPlayListsDao().deleteTrackById(trackId)
    }

}