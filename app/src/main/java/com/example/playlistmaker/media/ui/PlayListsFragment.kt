package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListFragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayListsBinding
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListsState
import com.example.playlistmaker.media.ui.viewModel.PlayListsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment : Fragment() {

    private lateinit var binding: FragmentPlayListsBinding
    private val viewModel by viewModel<PlayListsViewModel>()
    private lateinit var adapter: PlayListsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlayListsAdapter()
        binding.placeholderMessage.text = requireArguments().getString(MESSAGE_HOLDER)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        viewModel.stateLiveData
            .onEach { state -> handleState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getAllPlayLists()



        binding.buttonAddNewOlayList.setOnClickListener {

            findNavController().navigate(R.id.action_mediaFragment_to_newPlayListFragment)

        }
    }

    private fun handleState(state: NewPlayListsState) {
        when (state) {
            is NewPlayListsState.Empty -> {
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.imageHolder.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
            is NewPlayListsState.NewPlayListsLoaded -> {
                val playLists = state.tracks
                binding.placeholderMessage.visibility = View.GONE
                binding.imageHolder.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.playLists = playLists as ArrayList<PlayListsModels>
                adapter.notifyDataSetChanged()
                (activity as MainActivity).openBottomNavigation()

            }
        }
    }

    companion object {

        const val MESSAGE_HOLDER = "MESSAGE_HOLDER"

        fun newInstance(message: String) = PlayListsFragment().apply {
            arguments = Bundle().apply {
                putString(MESSAGE_HOLDER, message)
            }

        }
    }
}

