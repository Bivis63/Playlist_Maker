package com.example.playlistmaker.settings.data

interface ThemeRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(isEnabled: Boolean)
}