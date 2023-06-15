package com.example.playlistmaker.creator


import android.content.Context
import com.example.playlistmaker.player.data.impl.AudioPlayerImpl
import com.example.playlistmaker.player.domain.AudioPlayer
import com.example.playlistmaker.player.domain.AudioPlayerIteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerIteractorImpl
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.impl.TrackHistoryImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.TrackHistory
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl

import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.google.gson.Gson

object Creator {

    private fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl()
    }
    fun providePlayerInteractor():AudioPlayerIteractor  {
        return AudioPlayerIteractorImpl(getAudioPlayer())
    }

//   private fun getTrackHistory(context: Context,gson: Gson):TrackHistory{
//       return TrackHistoryImpl(context,gson)
//   }
//    private fun trackRepository(network:NetworkClient):TrackRepository{
//        return TrackRepositoryImpl(network)
//    }
//    fun provideSearchIterator (context: Context,gson: Gson,network:NetworkClient):SearchInteractor{
//        return SearchInteractorImpl(getTrackHistory(context, gson), trackRepository(network))
//    }
}