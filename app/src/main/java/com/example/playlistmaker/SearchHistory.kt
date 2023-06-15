package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class SearchHistory(val sharedPreferences: SharedPreferences) {


    private var searchHistoryList = ArrayList<Track>()

    fun addTrack(track: Track) {
        val trackIndex = searchHistoryList.indexOfFirst { it.trackId == track.trackId }
        if (searchHistoryList.size < 10) {
            if (trackIndex == -1) {
                searchHistoryList.add(0, track)
            } else {
                replacingDuplicateElement(trackIndex)
            }
        } else {
            if (trackIndex == -1) {
                searchHistoryList.removeAt(searchHistoryList.lastIndex)
                searchHistoryList.add(0, track)
            } else {
                replacingDuplicateElement(trackIndex)

            }
        }
        save()
    }

    fun replacingDuplicateElement(index: Int) {
        val newTrack = searchHistoryList[index]
        searchHistoryList.removeAt(index)
        searchHistoryList.add(0, newTrack)
    }

    fun ClearSearchHistoryList() {
        searchHistoryList.clear()
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    fun save() {
        val json = Gson().toJson(searchHistoryList)
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY, json)
            .apply()
    }

    fun load(): ArrayList<Track> {
        val json = sharedPreferences.getString(TRACKS_HISTORY, "[]")
        val type = object : TypeToken<List<Track>>() {}.type
        searchHistoryList = Gson().fromJson(json, type)
        return searchHistoryList
    }

    companion object {
        const val TRACKS_HISTORY = "tracks_history"
    }
}