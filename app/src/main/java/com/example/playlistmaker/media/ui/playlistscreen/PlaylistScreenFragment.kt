package com.example.playlistmaker.media.ui.playlistscreen

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
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


        viewModel.getPlaylistById(playlistModelId)

        viewModel.observeState().observe(viewLifecycleOwner) {
            if (it != null) {

                playlistModel = it
                viewModel.getAllPlaylistTracks(playlistModel!!.tracks)
                viewModel.getDurationAllTracks()
//                binding.trackCount.text = setTracksCount(playlistModel.trackCount)
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

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.visibility = View.VISIBLE

        binding.trackCount.text = "${setTracksCount(playlistModel.trackCount)}"
        binding.playlistName.text = playlistModel?.name
        binding.year.text = playlistModel.description
        val imageUri = playlistModel?.imageUri?.let { Uri.parse(it) }

        Glide.with(requireContext())
            .load(imageUri)
            .placeholder(R.drawable.newplaceholder)
            .centerCrop()
            .into(binding.imageNewPlaylistImage)


        (activity as MainActivity).hideBottomNavigation()

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
    fun onLongClick(track:Track){
        openDialog(track.trackId.toLong())

    }

    private fun openDialog(trackId:Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNegativeButton("НЕТ") { dialog, which ->
                Toast.makeText(requireContext(), "нажал кнопку нет", Toast.LENGTH_SHORT).show()
            }
            .setPositiveButton("ДА") { dialog, which ->
                Toast.makeText(requireContext(), "нажал кнопку да", Toast.LENGTH_SHORT).show()
                viewModel.deleteTrackFromPlaylist(trackId)
                val playListsFragment = parentFragmentManager.findFragmentByTag("PlayListsFragment") as? PlayListsFragment
                playListsFragment?.refreshData()
                binding.trackCount.text = setTracksCount(viewModel.observePlaylistTrackState().value?.size ?: 0)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).openBottomNavigation()
    }

    private fun setTracksCount(count: Int): String {
        return resources.getQuantityString(R.plurals.track_count, count, count)
    }

    private fun showContent(tracks: List<Track>) {
        binding.recyclerView.visibility = View.VISIBLE
        adapter.setTracks(tracks)
    }
}