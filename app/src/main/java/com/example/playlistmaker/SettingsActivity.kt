package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.ActivitySettings2Binding

const val THEM_SWITCHER = "THEM_SWITCHER"

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettings2Binding by lazy {
        ActivitySettings2Binding.inflate(layoutInflater)
    }

    @SuppressLint("WrongViewCast", "MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences(THEM_SWITCHER, MODE_PRIVATE)
        val replacement = sharedPrefs.getBoolean(THEM_SWITCHER, false)
        binding.themeSwitcher.isChecked = replacement
        (applicationContext as App).switchTheme(replacement)

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            if (cheked) {
                (applicationContext as App).switchTheme(cheked)

                sharedPrefs.edit()
                    .putBoolean(THEM_SWITCHER, cheked)
                    .apply()
            } else
                sharedPrefs.edit()
                    .putBoolean(THEM_SWITCHER, cheked)
                    .apply()
            (applicationContext as App).switchTheme(cheked)

        }

        binding.arrowBack.setOnClickListener {
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