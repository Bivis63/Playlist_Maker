package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingInteractor {
    fun shareApp(url:String)

    fun openTerms(url:String)

    fun openSupport(emailData : EmailData)
}