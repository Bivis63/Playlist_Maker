package com.example.playlistmaker

import java.io.Serializable

data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName:String,
    val releaseDate:String,
    val primaryGenreName:String,
    val country :String,
    val previewUrl:String,
): Serializable{
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
