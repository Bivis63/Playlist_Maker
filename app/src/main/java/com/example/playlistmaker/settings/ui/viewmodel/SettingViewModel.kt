package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.usecase.ThemeUseCase
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData


class SettingViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeUseCase: ThemeUseCase
) : ViewModel() {
    private val _isDarkThemeEnable = MutableLiveData<Boolean>()
    val isDarkThemeEnable: LiveData<Boolean> = _isDarkThemeEnable

    init {
        _isDarkThemeEnable.value = themeUseCase.isDarkThemeEnable()
    }

    fun setTheme(isDark: Boolean) {
        themeUseCase.setTheme(isDark)
        _isDarkThemeEnable.value = isDark
    }

    fun shareApp(url: String) {
        sharingInteractor.shareApp(url)
    }

    fun openTerms(url: String) {
        sharingInteractor.openTerms(url)
    }

    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }

}