package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.databinding.ActivitySettings2Binding

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettings2Binding by lazy {
        ActivitySettings2Binding.inflate(layoutInflater)
    }

    @SuppressLint("WrongViewCast", "MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.arrowBack.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }
        binding.support.setOnClickListener {
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
        binding.termsOfUse.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer)))
            startActivity(shareIntent)
        }
        binding.share.setOnClickListener {
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