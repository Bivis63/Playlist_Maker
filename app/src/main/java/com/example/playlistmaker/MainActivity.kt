package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_search = findViewById<Button>(R.id.buttonSearch)
        val button_media = findViewById<Button>(R.id.buttonMedia)
        val button_setting = findViewById<Button>(R.id.buttonSettings)
        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на поиск!", Toast.LENGTH_SHORT).show()
            }
        }
        button_media.setOnClickListener{
            Toast.makeText(this@MainActivity,"нажали на медиатеку!",Toast.LENGTH_SHORT).show()
        }
        button_setting.setOnClickListener{
            Toast.makeText(this@MainActivity,"Нажали на настройки!",Toast.LENGTH_SHORT).show()
        }
        button_search.setOnClickListener(buttonSearchClickListener)


    }
}