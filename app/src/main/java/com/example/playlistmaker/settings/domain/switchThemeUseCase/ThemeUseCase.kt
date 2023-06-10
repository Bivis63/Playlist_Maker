package com.example.playlistmaker.settings.domain.switchThemeUseCase

import com.example.playlistmaker.settings.data.ThemeRepository

class ThemeUseCase(private val themeRepository: ThemeRepository) {
    fun isDarkThemeEnable() : Boolean{
        return themeRepository.isDarkThemeEnabled()
    }
    fun setTheme(isDark : Boolean){
        return themeRepository.setDarkThemeEnabled(isDark)
    }
}