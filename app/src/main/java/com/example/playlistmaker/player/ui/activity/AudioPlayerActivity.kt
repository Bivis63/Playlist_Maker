package com.example.playlistmaker.player.ui.activity


import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListsState
import com.example.playlistmaker.player.ui.AudioPlayerAdapter
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.ITEM
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var adapter: AudioPlayerAdapter

    private lateinit var songUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val shapeDrawable = GradientDrawable()
//        shapeDrawable.cornerRadius = 20f

        val item = intent.getSerializableExtra(ITEM) as Track

        adapter = AudioPlayerAdapter()

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter


        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            binding.buttonAddNewOlayList.background=shapeDrawable

        }
        viewModel.statePlayListsLiveData
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)

        viewModel.getAllPlayLists()



        viewModel.stateLiveData.observe(this) {
            render(it)
        }
        viewModel.favoriteLifeData.observe(this) {
            render(it)
        }

        binding.backToTrackList.setOnClickListener {
            finish()
        }

        showTrack(item)

        viewModel.isFavorite(item.trackId)

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked(item)
        }

    }

    private fun handleState(state: NewPlayListsState) {
        when (state) {
            is NewPlayListsState.Empty -> {
                binding.recyclerView.visibility = View.GONE
            }
            is NewPlayListsState.NewPlayListsLoaded -> {
                val playLists = state.tracks
                binding.recyclerView.visibility = View.VISIBLE
                adapter.playLists = playLists as ArrayList<PlayListsModels>
                adapter.notifyDataSetChanged()
            }
        }
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

    @SuppressLint("ResourceAsColor")
    private fun render(state: PlayerState) {
        val colorStateList: ColorStateList
        when (state) {
            is PlayerState.Stoped -> {
                binding.playButton.isEnabled = true
                binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
                binding.trackTimeNow.setText(R.string._00_00)
            }
            is PlayerState.Playing -> {
                Log.i("COROUTINE", "play")
                binding.playButton.setImageResource(R.drawable.baseline_pause_24)
            }
            is PlayerState.Paused -> {
                Log.i("COROUTINE", "pause")
                binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            }
            is PlayerState.Preparing -> {

            }
            is PlayerState.PlayingTimeNow -> {
                binding.trackTimeNow.text = state.playingTime
            }
            is PlayerState.StateFavorite -> {
                if (state.isFavorite) {
                    colorStateList =
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
                    binding.likeButton.setImageTintList(colorStateList)
                    binding.likeButton.setImageResource(R.drawable.like)

                } else {
                    colorStateList =
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                    binding.likeButton.setImageTintList(colorStateList)
                    binding.likeButton.setImageResource(R.drawable.heart)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}