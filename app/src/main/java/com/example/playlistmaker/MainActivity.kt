package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_search = findViewById<Button>(R.id.buttonSearch)
        val button_media = findViewById<Button>(R.id.buttonMedia)
        val button_setting = findViewById<Button>(R.id.buttonSettings)



        button_setting.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
        button_search.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }
        button_media.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }
    }
}
