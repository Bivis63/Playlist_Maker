package com.example.playlistmaker.media.ui.NewPlayLists


import com.example.playlistmaker.media.domain.db.models.PlayListsModels


interface NewPlayListsState {

    object Empty : NewPlayListsState

    data class NewPlayListsLoaded(val tracks : List<PlayListsModels>):NewPlayListsState

}