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
        binding.trackCount.text = entity.description
        val imageUri = entity.imageUri?.let { Uri.parse(it) }

        Glide.with(itemView)
            .load(imageUri)
            .placeholder(R.drawable.newplaceholder)
            .into(binding.playlistCover)
        itemView.setOnClickListener {
            clickListener.onClick(entity)
        }
    }
    fun interface ClickListener {
        fun onClick(playlistModel: PlayListsModels)
    }
}
