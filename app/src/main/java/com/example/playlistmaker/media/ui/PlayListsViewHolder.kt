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
import java.io.File


class PlayListsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemPlaylistBinding.bind(view)

    fun bind(entity: PlayListsModels) {
        binding.NamePlayList.text = entity.name
        binding.descriptionPlayList.text = entity.description
        val imageUri = entity.imageUri?.let { Uri.parse(it) }

        Glide.with(itemView)
            .load(imageUri)
            .placeholder(R.drawable.newplaceholder)
            .into(binding.IconPlayList)
    }
}