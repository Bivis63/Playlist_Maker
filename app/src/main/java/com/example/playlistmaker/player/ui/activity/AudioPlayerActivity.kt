package com.example.playlistmaker.player.ui.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

const val ITEM = "item"

class AudioPlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var songUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val item = intent.getSerializableExtra(ITEM) as Track


        viewModel.stateLiveData.observe(this) {
            render(it)
        }

        binding.backToTrackList.setOnClickListener {
            finish()
        }

        showTrack(item)

    }

    private fun showTrack(item: Track) {
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

        viewModel.preparePlayer(songUrl)

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

    }


    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.Stoped -> {
                binding.playButton.isEnabled = true
                binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.trackTimeNow.setText(R.string._00_00)
            }
            is PlayerState.Playing -> {
                binding.playButton.setImageResource(R.drawable.baseline_pause_24)
            }
            is PlayerState.Paused -> {
                binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            }
            is PlayerState.Preparing -> {

            }
            is PlayerState.PlayingTimeNow -> {
                binding.trackTimeNow.text = state.playingTime
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }


}