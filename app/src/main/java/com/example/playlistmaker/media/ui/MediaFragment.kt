package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMedia2Binding
import com.google.android.material.tabs.TabLayoutMediator


class MediaFragment : Fragment() {

    private lateinit var binding: FragmentMedia2Binding
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedia2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedTrackMessage = resources.getString(R.string.media_is_empty)
        val playListsMessage = resources.getString(R.string.play_list_is_empty)

        binding.viewPager.adapter =
            MediaAdapter(childFragmentManager, lifecycle, selectedTrackMessage, playListsMessage)

        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.selected_traks)
                    1 -> tab.text = getString(R.string.play_lists)
                }
            }
        tabLayoutMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }

}