package com.example.playlistmaker.presentation.settingsPage.themSwitcher

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

import com.example.playlistmaker.presentation.settingsPage.THEM_SWITCHER

class App : Application() {
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(THEM_SWITCHER, MODE_PRIVATE)
        switchTheme(sharedPreferences.getBoolean(THEM_SWITCHER, darkTheme))
    }

    fun switchTheme(darkThemeEnable: Boolean) {
        darkTheme = darkThemeEnable
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnable) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}