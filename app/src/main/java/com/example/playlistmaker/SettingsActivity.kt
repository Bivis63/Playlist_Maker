package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)

        val arrowBack = findViewById<TextView>(R.id.arrowBack)
        val support = findViewById<ImageView>(R.id.support)
        val termsOfUse = findViewById<ImageView>(R.id.termsOfUse)
        val share = findViewById<ImageView>(R.id.share)


        arrowBack.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }
        support.setOnClickListener {
            val message = getResources().getString(R.string.thxForDevelopers)
            val theme = getResources().getString(R.string.messegeForDevelopers)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.Email)))
            shareIntent.putExtra(Intent.EXTRA_TEXT,message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,theme)
            startActivity(shareIntent)
        }
        termsOfUse.setOnClickListener {
            val shareIntent=Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer)))
            startActivity(shareIntent)
        }
        share.setOnClickListener {
            val shareText = getResources().getString(R.string.androidCourse)
            val sendIntent: Intent = Intent(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }
}