package com.example.playlistmaker.media.ui.History

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.example.playlistmaker.media.ui.viewModel.SelectedTracksViewModel
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter

import com.example.playlistmaker.util.ITEM
import org.koin.androidx.viewmodel.ext.android.viewModel


class SelectedTracksFragment : Fragment() {

    private lateinit var binding: FragmentSelectedTracksBinding
    private val viewModel by viewModel<SelectedTracksViewModel>()
    private var adapter = TrackAdapter { (onClick(it)) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectedTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyList.adapter = adapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.placeholderMessage.text = requireArguments().getString(MESSAGE_HOLDER_ON_TRACKS)
    }

    fun onClick(track: Track) {
        startActivity(Intent(requireContext(), AudioPlayerActivity::class.java).apply {
            putExtra(ITEM, track)
        })
    }

    private fun render(state: HistoryState) {
        when (state) {
            is HistoryState.Content -> showContent(state.tracks)
            is HistoryState.Empty -> showEmpty()
        }
    }

    companion object {
        const val MESSAGE_HOLDER_ON_TRACKS = "MESSAGE_HOLDER_ON_TRACKS"

        fun newInstance(message: String) = SelectedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(MESSAGE_HOLDER_ON_TRACKS, message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTrack()
    }

    private fun showEmpty() {
        binding.historyList.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.imageHolder.visibility = View.VISIBLE

    }

    private fun showContent(tracks: List<Track>) {
        binding.historyList.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.imageHolder.visibility = View.GONE
        adapter?.tracksList?.clear()
        adapter?.tracksList?.addAll(tracks)
        adapter?.notifyDataSetChanged()
    }
}