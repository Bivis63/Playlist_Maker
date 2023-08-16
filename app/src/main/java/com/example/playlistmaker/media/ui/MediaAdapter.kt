package com.example.playlistmaker.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.media.ui.History.SelectedTracksFragment


class MediaAdapter(
    fragmentManager: FragmentManager,
    lifecycle: androidx.lifecycle.Lifecycle,
    private val selectedTrackMessage: String,
    private val playListsMessage: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SelectedTracksFragment.newInstance(selectedTrackMessage)
            else -> PlayListsFragment.newInstance(playListsMessage)
        }
    }

}