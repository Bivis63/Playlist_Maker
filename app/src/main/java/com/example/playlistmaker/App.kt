package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

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