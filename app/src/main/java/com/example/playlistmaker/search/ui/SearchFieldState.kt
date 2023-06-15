package com.example.playlistmaker.search.ui

sealed class SearchFieldState {
    class SearchTextEmpty(val needClear: Boolean) : SearchFieldState()
    object SearchTextEntered : SearchFieldState()
}