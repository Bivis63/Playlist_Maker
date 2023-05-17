package com.example.playlistmaker.data

import com.example.playlistmaker.data.trackModels.Track

class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)
