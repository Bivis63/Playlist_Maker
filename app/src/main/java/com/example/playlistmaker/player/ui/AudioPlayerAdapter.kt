package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.db.models.PlayListsModels

class AudioPlayerAdapter(private val clickListener: AudioPlayerViewHolder.ClickListener):RecyclerView.Adapter<AudioPlayerViewHolder>() {

    var playLists = ArrayList<PlayListsModels>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_sheet_playlists,parent,false)
        return AudioPlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
      return playLists.size
    }

    override fun onBindViewHolder(holder: AudioPlayerViewHolder, position: Int) {
        holder.bind(playLists[position],clickListener)
    }
}