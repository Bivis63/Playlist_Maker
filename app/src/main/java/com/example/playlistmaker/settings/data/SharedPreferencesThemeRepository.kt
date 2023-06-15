package com.example.playlistmaker.settings.data

import android.content.SharedPreferences

import com.example.playlistmaker.settings.ui.activity.THEM_SWITCHER

class SharedPreferencesThemeRepository(private val sharedPreferences: SharedPreferences): ThemeRepository {
    override fun isDarkThemeEnabled(): Boolean {
        return  sharedPreferences.getBoolean(THEM_SWITCHER,false)
    }

    override fun setDarkThemeEnabled(isEnabled: Boolean) {
        return sharedPreferences
            .edit()
            .putBoolean(THEM_SWITCHER,isEnabled)
            .apply()
    }

}