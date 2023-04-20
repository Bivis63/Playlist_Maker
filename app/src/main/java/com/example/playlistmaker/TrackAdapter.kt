package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.SongListBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrackAdapter(val trackListener: OnTrackClickListener) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    var tracksList = ArrayList<Track>()
        set(newTracks) {
            val diffCallBack = TracksDiffCallBack(field, newTracks)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newTracks
            diffResult.dispatchUpdatesTo(this)
        }

    class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = SongListBinding.bind(item)

        fun bind(track: Track, trackListener: OnTrackClickListener) = with(binding) {
            nameTrack.text = track.trackName
            nameArtist.text = track.artistName
            timeTrack.text = track.trackTimeMillis.toString()
            timeTrack.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            itemView.setOnClickListener {
                trackListener.onClick(track)
            }

            Glide.with(itemView)
                .load(track.artworkUrl100)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_list, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position], trackListener)

    }

    interface OnTrackClickListener {
        fun onClick(track: Track)
    }

    fun removeTrackList() {
        tracksList = ArrayList()
    }

}
