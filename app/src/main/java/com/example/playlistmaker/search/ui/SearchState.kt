package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.models.Track

sealed class SearchState {
    object Loading : SearchState()
    object NothingFound : SearchState()
    object CommunicationProblems : SearchState()
    data class History(val track: List<Track>) : SearchState()
    data class Tracks(val tracks: List<Track>) : SearchState()

}
