package com.example.playlistmaker.data.sharedPreference

import android.content.Context
import com.example.playlistmaker.data.trackModels.Track

class SharedPreferenceHelper(private val contex: Context) {
    private val PREFS_NAME = "myPreferences"
    private val TRACK_NAME_KEY = "trackName"
    private val ARTIST_NAME_KEY = "artistName"
    private val TRACK_TIME_KEY = "trackTimeMillis"
    private val ARTWORK_URL_KEY = "artworkUrl100"
    private val COLLECTION_NAME_KEY = "collectionName"
    private val RELEASE_DATA_KEY = "releaseDate"
    private val PRIMORY_GENRE_NAME = "primaryGenreName"
    private val COUNTRY_KEY = "country"
    private val PREVIEW_URL_KEY = "previewUrl"
    private val ID_KEY = "trackId"

    fun saveTrack(track: Track) {
        val sharedPreferences = contex.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(TRACK_NAME_KEY, track.trackName)
            putString(ARTIST_NAME_KEY, track.artistName)
            putInt(TRACK_TIME_KEY, track.trackTimeMillis)
            putString(ARTWORK_URL_KEY, track.artworkUrl100)
            putString(COLLECTION_NAME_KEY, track.collectionName)
            putString(RELEASE_DATA_KEY, track.releaseDate)
            putString(PRIMORY_GENRE_NAME, track.primaryGenreName)
            putString(COUNTRY_KEY, track.country)
            putString(PREVIEW_URL_KEY, track.previewUrl)
            putInt(ID_KEY, track.trackId)
            apply()
        }
    }

    fun getSavedTrack(): Track? {
        val sharedPreferences = contex.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val trackName = sharedPreferences.getString(TRACK_NAME_KEY, "")
        val artistName = sharedPreferences.getString(ARTIST_NAME_KEY, "")
        val trackTimeMills = sharedPreferences.getInt(TRACK_TIME_KEY, 0)
        val artworkUrl = sharedPreferences.getString(ARTWORK_URL_KEY, "")
        val collectionName = sharedPreferences.getString(COLLECTION_NAME_KEY, "")
        val releaseData = sharedPreferences.getString(RELEASE_DATA_KEY, "")
        val primaryGenreName = sharedPreferences.getString(PRIMORY_GENRE_NAME, "")
        val country = sharedPreferences.getString(COUNTRY_KEY, "")
        val previewUrl = sharedPreferences.getString(PREVIEW_URL_KEY, "")
        val trackId = sharedPreferences.getInt(ID_KEY, 0)

        if (trackName.isNullOrEmpty() || artistName.isNullOrEmpty() || artworkUrl.isNullOrEmpty() || collectionName.isNullOrEmpty()
            || releaseData.isNullOrEmpty() || primaryGenreName.isNullOrEmpty() || country.isNullOrEmpty() || previewUrl.isNullOrEmpty()
        ) {
            return null
        } else {
            return Track(
                trackId,
                trackName,
                artistName,
                trackTimeMills,
                artworkUrl,
                collectionName,
                releaseData,
                primaryGenreName,
                country,
                previewUrl
            )
        }
    }
}