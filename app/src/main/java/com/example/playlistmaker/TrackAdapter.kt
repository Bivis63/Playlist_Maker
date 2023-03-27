package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.SongListBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext
class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    var tracksList = ArrayList<Track>()
        set(newTracks) {
            val diffCallBack = TracksDiffCallBack(field, newTracks)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newTracks
            diffResult.dispatchUpdatesTo(this)
        }
    class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = SongListBinding.bind(item)

        fun bind(track: Track) = with(binding) {
            nameTrack.text = track.trackName
            nameArtist.text = track.artistName
            timeTrack.text = track.trackTimeMillis.toString()
            timeTrack.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .transform(RoundedCorners(10))
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
        holder.bind(tracksList[position])
//        попробывал удалить трек по нажатию
//        holder.itemView.setOnClickListener {
//            tracksList.removeAt(position)
//            holder.bind(tracksList[position])
//        }

    }
     fun removeTrackList(){
        tracksList= ArrayList()
    }
}
