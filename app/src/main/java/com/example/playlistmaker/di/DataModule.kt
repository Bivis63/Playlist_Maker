package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.impl.AudioPlayerImpl
import com.example.playlistmaker.player.domain.AudioPlayer
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.impl.TrackHistoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.TrackHistory
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.util.THEM_SWITCHER
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<ExternalNavigator>{
        ExternalNavigatorImpl(get())
    }

    single{
        Gson()
    }

    single<TrackHistory>{
        TrackHistoryImpl(get(),get())
    }

    single<NetworkClient>{
        RetrofitNetworkClient(get())
    }

    factory<AudioPlayer>{
        AudioPlayerImpl()
    }

    single <SharedPreferences>{
        androidContext().getSharedPreferences(THEM_SWITCHER, Context.MODE_PRIVATE)
    }

}