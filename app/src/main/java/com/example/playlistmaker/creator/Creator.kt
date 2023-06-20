package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.impl.AudioPlayerImpl
import com.example.playlistmaker.player.domain.AudioPlayer
import com.example.playlistmaker.player.domain.AudioPlayerIteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerIteractorImpl


object Creator {

    private fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl()
    }
    fun providePlayerInteractor():AudioPlayerIteractor  {
        return AudioPlayerIteractorImpl(getAudioPlayer())
    }


}