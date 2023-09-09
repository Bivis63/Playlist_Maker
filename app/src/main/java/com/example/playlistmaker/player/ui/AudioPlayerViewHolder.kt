package com.example.playlistmaker.player.ui

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemBottomSheetPlaylistsBinding
import com.example.playlistmaker.media.domain.db.models.PlayListsModels

class AudioPlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemBottomSheetPlaylistsBinding.bind(view)

    fun bind(entity: PlayListsModels,clickListener: ClickListener) {

        binding.titlePlayList.text = entity.name
        binding.trackCount.text = setTracksCount(entity.trackCount)
        val imageUri = entity.imageUri?.let { Uri.parse(it) }

        Glide.with(itemView)
            .load(imageUri)
            .placeholder(R.drawable.newplaceholder)
            .into(binding.playlistCover)
        itemView.setOnClickListener {
            clickListener.onClick(entity)
        }
    }
    private fun setTracksCount(count: Int): String {
        val tracksWord: String = when {
            count % 100 in 11..19 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
        return "$count $tracksWord"
    }
    fun interface ClickListener {
        fun onClick(playlistModel: PlayListsModels)
    }
}
