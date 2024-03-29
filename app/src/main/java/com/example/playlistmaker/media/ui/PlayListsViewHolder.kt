package com.example.playlistmaker.media.ui

import android.net.Uri
import android.os.Environment
import android.view.RoundedCorner
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.search.domain.models.Track
import java.io.File


class PlayListsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemPlaylistBinding.bind(view)

    fun bind(entity: PlayListsModels,clickListener:ClickListener) {
        binding.NamePlayList.text = entity.name
        binding.descriptionPlayList.text = setTracksCount(entity.trackCount)
        val imageUri = entity.imageUri?.let { Uri.parse(it) }

        Glide.with(itemView)
            .load(imageUri)
            .placeholder(R.drawable.newplaceholder)
            .into(binding.IconPlayList)
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