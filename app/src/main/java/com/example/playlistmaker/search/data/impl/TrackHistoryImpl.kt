package com.example.playlistmaker.search.data.impl
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import com.example.playlistmaker.search.domain.TrackHistory
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SEARCH_HISTORY = "search_history"

class TrackHistoryImpl(context: Context, private val gson: Gson) : TrackHistory {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SEARCH_PREFERENCES, Application.MODE_PRIVATE
    )

    override fun addTrack(track: Track) {
        val trackListString = loadListString()
        if (trackListString == null) {
            save(arrayListOf(track))
            return
        }
        val trackList = trackListString.toTrackList()
        if (trackList.contains(track)){
            trackList.remove(track)
        }
        if (trackList.size >= 10) {
            trackList.removeAt(trackList.size - 1)
        }
        trackList.add(0, track)
        save(trackList)

    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }

    override fun getHistory(): ArrayList<Track> {
        return loadListString()?.toTrackList() ?: ArrayList<Track>()
    }


    private fun loadListString(): String? {

        return sharedPreferences.getString(SEARCH_HISTORY, null)
    }

    private fun save(list: ArrayList<Track>) {
        sharedPreferences
            .edit()
            .putString(SEARCH_HISTORY, gson.toJson(list))
            .apply()
    }

    private fun String.toTrackList() = gson.fromJson<ArrayList<Track>>(
        this, object : TypeToken<ArrayList<Track>>() {}.type
    )

    companion object {
        const val SEARCH_PREFERENCES = "search"
    }

}