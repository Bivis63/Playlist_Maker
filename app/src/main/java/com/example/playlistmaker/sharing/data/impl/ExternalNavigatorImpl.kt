package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {


    override fun shareLink(url: String) {
        try {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, url)
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(
                Intent.createChooser(sendIntent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.resources.getString(R.string.error_has_occurred), Toast.LENGTH_SHORT).show()
        }
    }

    override fun openLink(url: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.resources.getString(R.string.error_has_occurred), Toast.LENGTH_SHORT).show()
        }
    }

    override fun openEmail(
        emailData: EmailData
    ) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
                putExtra(Intent.EXTRA_TEXT, emailData.message)
                putExtra(Intent.EXTRA_SUBJECT, emailData.theme)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.resources.getString(R.string.exeption_on_mail), Toast.LENGTH_SHORT).show()
        }
    }

}