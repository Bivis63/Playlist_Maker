package com.example.playlistmaker.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabLayoutMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedTrackMessage = resources.getString(R.string.media_is_empty)
        val playListsMessage = resources.getString(R.string.play_list_is_empty)

        binding.viewPager.adapter = MediaAdapter(supportFragmentManager,lifecycle,selectedTrackMessage, playListsMessage)

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab,position ->
            when (position){
                0-> tab.text = getString(R.string.selected_traks)
                1-> tab.text = getString(R.string.play_lists)
            }
        }
        tabLayoutMediator.attach()

            binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }

}