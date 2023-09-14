package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.db.models.PlayListsModels


class PlayListsAdapter():RecyclerView.Adapter<PlayListsViewHolder>() {
    var playLists = ArrayList<PlayListsModels>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist,parent,false)
        return PlayListsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playLists.size
    }

    override fun onBindViewHolder(holder: PlayListsViewHolder, position: Int) {
       holder.bind(playLists[position])
    }


}
