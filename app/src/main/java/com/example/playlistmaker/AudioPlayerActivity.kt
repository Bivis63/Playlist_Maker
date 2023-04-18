package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*
const val ITEM = "item"
class AudioPlayerActivity : AppCompatActivity() {
    lateinit var binding: ActivityAudioPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityAudioPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backToTrackList.setOnClickListener {
            finish()
        }

        val item = intent.getSerializableExtra(ITEM) as Track
        binding.apply {
            trackName.isSelected= true
            trackName.text=item.trackName
            artistName.text=item.artistName
            albumName.text = item.collectionName
            releaseDate.text = item.releaseDate.substringBefore("-")
            genreName.text = item.primaryGenreName
            counryName.text = item.country
            durationTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)


        }
        Glide.with(this)
            .load(item.getCoverArtwork())
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.newplaceholder)
            .into(binding.imageAlbum)

    }
}