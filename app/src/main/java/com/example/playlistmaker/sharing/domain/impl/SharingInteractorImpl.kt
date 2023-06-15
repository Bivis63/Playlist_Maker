package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData


class SharingInteractorImpl(private val externalNavigator: ExternalNavigator):
    SharingInteractor {
    override fun shareApp(url:String) {
        externalNavigator.shareLink(url)
    }

    override fun openTerms(url:String) {
        externalNavigator.openLink(url)
    }

    override fun openSupport(emailData : EmailData) {
        externalNavigator.openEmail(emailData)
    }
}