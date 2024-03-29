package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.databinding.SongListBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrackAdapter(
    private val onClick: (Track) -> Unit,
    private val onLongClick: ((Track) -> Unit)? = null
) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    var tracksList = ArrayList<Track>()
        set(newTracks) {
            val diffCallBack = TracksDiffCallBack(field, newTracks)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newTracks
            diffResult.dispatchUpdatesTo(this)
        }

    class TrackViewHolder(
        item: View,
        val onClick: (Track) -> Unit,
        val onLongClick: ((Track) -> Unit)? = null
    ) : RecyclerView.ViewHolder(item) {
        val binding = SongListBinding.bind(item)

        fun bind(track: Track) = with(binding) {
            nameTrack.text = track.trackName
            nameArtist.text = track.artistName
            timeTrack.text = track.trackTimeMillis.toString()
            timeTrack.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            itemView.setOnClickListener {
                onClick(track)
            }
            itemView.setOnLongClickListener {
                onLongClick?.invoke(track)
                true
            }


            Glide.with(itemView)
                .load(track.artworkUrl60)
                .transform(
                    RoundedCorners(
                        itemView.resources.getDimensionPixelSize(
                            R.dimen.track_list_album_corner_radius
                        )
                    )
                )
                .placeholder(R.drawable.newplaceholder)
                .into(imageUrl)

        }
    }

    fun setTracks(tracks: List<Track>) {
        tracksList.clear()
        tracksList.addAll(tracks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_list, parent, false)
        return TrackViewHolder(view, onClick, onLongClick)
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position])

    }

}
