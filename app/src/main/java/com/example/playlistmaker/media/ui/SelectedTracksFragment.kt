package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.media.ui.viewModel.SelectedTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SelectedTracksFragment : Fragment() {

    private lateinit var binding: FragmentMediaBinding
    private val viewModel by viewModel<SelectedTracksViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.placeholderMessage.text = requireArguments().getString(MESSAGE_HOLDER_ON_TRACKS)
    }

    companion object {
        const val MESSAGE_HOLDER_ON_TRACKS = "MESSAGE_HOLDER_ON_TRACKS"

        fun newInstance(message: String) = SelectedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(MESSAGE_HOLDER_ON_TRACKS, message)
            }
        }
    }
}