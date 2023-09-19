package com.example.playlistmaker.media.ui.playlistscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.util.TimeUtils.formatDuration
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.ui.PlayListsFragment
import com.example.playlistmaker.media.ui.viewModel.PlayListScreenViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.util.ITEM
import com.example.playlistmaker.util.PUT_EXTRA_PLAYLIST
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlaylistScreenFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistScreenBinding
    private val viewModel by viewModel<PlayListScreenViewModel>()
    private var adapter = TrackAdapter(
        onClick = { onClick(it) },
        onLongClick = { onLongClick(it) }
    )
    private lateinit var playlistModel: PlayListsModels


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistModel = (arguments?.getSerializable(PUT_EXTRA_PLAYLIST) as? PlayListsModels)!!


        val playlistModelId = playlistModel.id
        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        val bottomSheetBehaviorPlaylist =
            BottomSheetBehavior.from(binding.standardBottomSheet).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }



        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheetBehaviorPlaylist.isHideable = false
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        bottomSheetBehaviorPlaylist.isHideable = true
                        bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_HIDDEN
                        adapter.notifyDataSetChanged()
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        })

        viewModel.getPlaylistById(playlistModelId)

        viewModel.observeState().observe(viewLifecycleOwner) {
            if (it != null) {
                playlistModel = it
                viewModel.getAllPlaylistTracks(playlistModel!!.tracks)
                viewModel.getDurationAllTracks()

            }
        }

        viewModel.observePlaylistTrackState().observe(viewLifecycleOwner) {
            if (it != null) {
                showContent(it)
                viewModel.getDurationAllTracks()
            }
        }
        viewModel.observePlaylistTrackCount().observe(viewLifecycleOwner) { trackCount ->
            binding.trackCount.text = setTracksCount(trackCount)
        }

        viewModel.observeDurationState().observe(viewLifecycleOwner) {
            if (it != null) {
                val duration = SimpleDateFormat("mm", Locale.getDefault()).format(it).toInt()
                val formattedDuration =
                    resources.getQuantityString(R.plurals.minutes, duration, duration)
                binding.time.text = formattedDuration

            }
        }

        viewModel.observeDeletePlaylistState().observe(viewLifecycleOwner) { completed ->
            if (completed) {
                findNavController().navigateUp()
            }
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.visibility = View.VISIBLE


        binding.sharePlaylist.setOnClickListener {
            sharePlaylist()
        }

        updatePlaylistInfo()

        (activity as MainActivity).hideBottomNavigation()

        binding.share.setOnClickListener {
            sharePlaylist()
        }

        binding.playlistMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.trackCountBottomSheet.text = "${setTracksCount(playlistModel.trackCount)}"
        }

        binding.deletePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            openDialogDeletePlaylist()
        }
        val bundle = Bundle().apply {
            putSerializable("playlistModel", playlistModel)
        }
        binding.editInformation.setOnClickListener {
            findNavController().navigate(R.id.action_playlistScreenFragment_to_editPlaylistFragment,bundle)
        }

        binding.backToMedia.setOnClickListener {
            findNavController().navigateUp()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as? MainActivity)?.openBottomNavigation()
                findNavController().navigateUp()
            }
        }
        onBackPressedDispatcher.addCallback(callback)
    }

    fun onClick(track: Track) {
        val bundle = bundleOf(ITEM to track)
        findNavController().navigate(
            R.id.action_playlistScreenFragment_to_audioPlayerFragment,
            bundle
        )
    }

    fun onLongClick(track: Track) {
        openDialog(track.trackId.toLong())

    }

    private fun openDialog(trackId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_track))
            .setMessage(resources.getString(R.string.want_to_delete_a_track_on_playlist))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
                    state = BottomSheetBehavior.STATE_COLLAPSED
                }
                bottomSheetBehavior.isHideable = false
            }
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(trackId)
                val playListsFragment =
                    parentFragmentManager.findFragmentByTag("PlayListsFragment") as? PlayListsFragment
                playListsFragment?.refreshData()
                binding.trackCount.text =
                    setTracksCount(viewModel.observePlaylistTrackState().value?.size ?: 0)
            }
            .show()
    }
    private fun updatePlaylistInfo() {
        binding.trackCount.text = setTracksCount(playlistModel.trackCount)
        binding.playlistName.text = playlistModel?.name
        binding.year.text = playlistModel.description
        binding.titlePlayList.text = playlistModel?.name
        val imageUri = playlistModel?.imageUri?.let { Uri.parse(it) }

        Glide.with(requireContext())
            .load(imageUri)
            .placeholder(R.drawable.newplaceholder)
            .centerCrop()
            .into(binding.imageNewPlaylistImage)

        Glide.with(requireContext())
            .load(imageUri)
            .placeholder(R.drawable.newplaceholder)
            .centerCrop()
            .into(binding.playlistCover)
    }

    private fun openDialogDeletePlaylist() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_playlist))
            .setMessage(resources.getString(R.string.want_to_delete_a_playlist))
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->

            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                viewModel.deletePlaylist(playlistModel.id)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).openBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        if (playlistModel.trackCount==0){
            Toast.makeText(requireContext(),resources.getString(R.string.playlist_is_empty),Toast.LENGTH_SHORT).show()
        }

        val playlistModelId = playlistModel.id
        viewModel.getPlaylistById(playlistModelId)

        viewModel.observeState().observe(viewLifecycleOwner) {
            if (it != null) {
                playlistModel = it
                viewModel.getAllPlaylistTracks(playlistModel!!.tracks)
                viewModel.getDurationAllTracks()

                updatePlaylistInfo()
            }
        }
    }

    private fun setTracksCount(count: Int): String {
        return resources.getQuantityString(R.plurals.track_count, count, count)
    }

    private fun showContent(tracks: List<Track>) {
        val sortedTracks = tracks.sortedByDescending { it.addingTime }
        binding.recyclerView.visibility = View.VISIBLE
        adapter.setTracks(sortedTracks)


    }

    private fun sharePlaylist() {
        val tracks = viewModel.observePlaylistTrackState().value ?: emptyList()
        if (tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.no_have_tracks),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val shareText = generateShareText()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }


    private fun generateShareText(): String {
        val playlistName = playlistModel.name
        val playlistDescription = playlistModel.description
        val tracks = viewModel.observePlaylistTrackState().value ?: emptyList()

        val sb = StringBuilder()
        sb.append("$playlistName\n")
        sb.append("$playlistDescription\n")
        sb.append("${setTracksCount(tracks.size)} \n")

        for ((index, track) in tracks.withIndex()) {
            sb.append(
                "${index + 1}. ${track.artistName} - ${track.trackName} (${
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTimeMillis)
                })\n"
            )
        }

        return sb.toString()
    }
}