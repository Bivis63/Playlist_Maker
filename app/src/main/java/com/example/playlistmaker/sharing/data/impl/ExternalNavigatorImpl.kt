package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {


    override fun shareLink(url: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(Intent.createChooser(sendIntent, null))
    }

    override fun openLink(url: String) {
        val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(shareIntent)
    }

    override fun openEmail(
        emailData: EmailData
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_TEXT, emailData.message)
            putExtra(Intent.EXTRA_SUBJECT, emailData.theme)
        }
        context.startActivity(intent)

    }

}