package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*

const val ITEM = "item"

class AudioPlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = null
    lateinit var binding: ActivityAudioPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var songUrl: String
    private val duration = setTrackDuration()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mainThreadHandler = Handler(Looper.getMainLooper())

        binding.backToTrackList.setOnClickListener {

            finish()
        }

        val item = intent.getSerializableExtra(ITEM) as Track
        binding.apply {
            trackName.isSelected = true
            trackName.text = item.trackName
            artistName.text = item.artistName
            albumName.text = item.collectionName
            releaseDate.text = item.releaseDate.substringBefore("-")
            genreName.text = item.primaryGenreName
            counryName.text = item.country
            durationTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        }
        songUrl = item.previewUrl
        Glide.with(this)
            .load(item.getCoverArtwork())
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.audio_player_album_corner_radius
                    )
                )
            )
            .placeholder(R.drawable.newplaceholder)
            .into(binding.imageAlbum)

        preparePlayer()
        binding.playButton.setOnClickListener {
            playBackControl()
            setIcon()
        }

    }


    private fun preparePlayer() {
        mediaPlayer.setDataSource(songUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mainThreadHandler?.removeCallbacks(duration)
            playerState = STATE_PREPARED
            setIcon()
            binding.trackTimeNow.text = "00:00"


        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler?.post(duration)

    }

    private fun pausePlayer() {
        mainThreadHandler?.removeCallbacks(duration)
        mediaPlayer.pause()
        setIcon()
        playerState = STATE_PAUSED
    }


    private fun playBackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                setIcon()
                startPlayer()
            }
        }
    }

    private fun setIcon() {
        when (playerState) {
            STATE_PLAYING -> binding.playButton.setImageResource(R.drawable.baseline_pause_24)
            STATE_PAUSED -> binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            else -> binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    private fun setTrackDuration(): Runnable {
        return object : Runnable {
            override fun run() {
                binding.trackTimeNow.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                mainThreadHandler?.postDelayed(this, DELAY)

            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacks(duration)
        mediaPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        setIcon()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
    }

}