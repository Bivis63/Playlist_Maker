package com.example.playlistmaker.sharing.data
import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink (url:String)
    fun openLink (url:String)
    fun openEmail(emailData : EmailData)
}