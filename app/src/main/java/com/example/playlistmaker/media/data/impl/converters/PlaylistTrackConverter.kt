package com.example.playlistmaker.media.data.impl.converters

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistTrackEntity
import com.example.playlistmaker.search.domain.models.Track
import java.util.*

class PlaylistTrackConverter {

    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            Date().time,
            track.artworkUrl60
        )
    }

    fun map(track: PlaylistTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            Date().time,
            track.artworkUrl60
        )
    }
}
