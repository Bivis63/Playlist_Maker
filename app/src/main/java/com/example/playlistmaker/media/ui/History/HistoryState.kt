package com.example.playlistmaker.media.ui.History

import com.example.playlistmaker.search.domain.models.Track

sealed interface HistoryState {

    data class Content(val tracks: List<Track>) : HistoryState

    object Empty : HistoryState
}