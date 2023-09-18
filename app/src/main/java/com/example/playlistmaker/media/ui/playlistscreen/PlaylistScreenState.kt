package com.example.playlistmaker.media.ui.playlistscreen

import com.example.playlistmaker.media.ui.History.HistoryState
import com.example.playlistmaker.search.domain.models.Track

sealed interface PlaylistScreenState{
    data class Content(val tracks: List<Track>) : PlaylistScreenState

    object Empty : PlaylistScreenState
}