package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {

    lateinit var arrowBackButton: TextView
    lateinit var supportButton: TextView
    lateinit var termsOfUseButton: TextView
    lateinit var shareButton: TextView

    @SuppressLint("WrongViewCast", "MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)

        arrowBackButton = findViewById(R.id.arrowBack)
        supportButton = findViewById(R.id.support)
        termsOfUseButton = findViewById(R.id.termsOfUse)
        shareButton = findViewById(R.id.share)


        arrowBackButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }
        supportButton.setOnClickListener {
            val message = getResources().getString(R.string.thxForDevelopers)
            val theme = getResources().getString(R.string.messegeForDevelopers)

            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.Email)))
                putExtra(Intent.EXTRA_TEXT, message)
                putExtra(Intent.EXTRA_SUBJECT, theme)
                startActivity(this)
            }
        }
        termsOfUseButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer)))
            startActivity(shareIntent)
        }
        shareButton.setOnClickListener {
            val shareText = getResources().getString(R.string.androidCourse)
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }
}