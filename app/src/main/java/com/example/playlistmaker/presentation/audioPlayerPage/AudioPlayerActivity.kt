package com.example.playlistmaker.presentation.audioPlayerPage


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.AudioPlayerInteractorImpl
import com.example.playlistmaker.data.sharedPreference.SharedPreferenceHelper
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {
    private val audioPlayerInteractor = AudioPlayerInteractorImpl()
    lateinit var songUrl: String
    private lateinit var sharedPreferencesHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioPlayerInteractor.binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(audioPlayerInteractor.binding.root)

        sharedPreferencesHelper=SharedPreferenceHelper(this)

        audioPlayerInteractor.binding.backToTrackList.setOnClickListener {
            finish()
        }
        val savedTrack = sharedPreferencesHelper.getSavedTrack()
        if (savedTrack != null) {
            audioPlayerInteractor.binding.apply {
                trackName.isSelected = true
                trackName.text = savedTrack.trackName
                artistName.text = savedTrack.artistName
                albumName.text = savedTrack.collectionName
                releaseDate.text = savedTrack.releaseDate.substringBefore("-")
                genreName.text = savedTrack.primaryGenreName
                counryName.text = savedTrack.country
                durationTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(savedTrack.trackTimeMillis)
            }
            songUrl = savedTrack.previewUrl
            Glide.with(this)
                .load(savedTrack.getCoverArtwork())
                .transform(
                    RoundedCorners(
                        resources.getDimensionPixelSize(
                            R.dimen.audio_player_album_corner_radius
                        )
                    )
                )
                .placeholder(R.drawable.newplaceholder)
                .into(audioPlayerInteractor.binding.imageAlbum)

            audioPlayerInteractor.preparePlayer(songUrl)
            audioPlayerInteractor.startPlayer()
        }


        audioPlayerInteractor.binding.playButton.setOnClickListener {
            audioPlayerInteractor.playBackControl()
            audioPlayerInteractor.setIcon()
        }
        audioPlayerInteractor.setTrackDuration()
        audioPlayerInteractor.preparePlayer(songUrl)
        audioPlayerInteractor.startPlayer()
        audioPlayerInteractor.pauseTrack()
        audioPlayerInteractor.playBackControl()
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pauseTrack()
    }
    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.mainThreadHandler?.removeCallbacks(audioPlayerInteractor.duration)
        audioPlayerInteractor.mediaPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        audioPlayerInteractor.setIcon()
    }

}