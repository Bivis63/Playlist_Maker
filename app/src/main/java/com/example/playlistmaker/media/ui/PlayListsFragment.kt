package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListFragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayListsBinding
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.ui.viewModel.PlayListsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment : Fragment() {

    private lateinit var binding: FragmentPlayListsBinding
    private val viewModel by viewModel<PlayListsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeholderMessage.text = requireArguments().getString(MESSAGE_HOLDER)

        binding.buttonAddNewOlayList.setOnClickListener {
            val newPlayList = NewPlayListFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            (activity as MainActivity).hideBottomNavigation()
            fragmentTransaction.replace(R.id.container_view,newPlayList)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
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

