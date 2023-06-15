package com.example.playlistmaker.search.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.data.impl.TrackHistoryImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchFieldState
import com.example.playlistmaker.search.ui.SearchState
import com.google.gson.Gson


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = _stateLiveData

    private val _stateSearchFieldLiveData = MutableLiveData<SearchFieldState>()
    val stateSearchFieldLiveData: LiveData<SearchFieldState> = _stateSearchFieldLiveData

    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null

    init {
        renderState(SearchState.Tracks(tracks = emptyList()))
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun clearSearchText() {
        _stateSearchFieldLiveData.postValue(SearchFieldState.SearchTextEmpty(true))
        renderState(SearchState.Tracks(tracks = emptyList()))
    }

    fun searchDebounce(changedText: String) {
        if (changedText.isEmpty()) {
            _stateSearchFieldLiveData.postValue(SearchFieldState.SearchTextEmpty(false))
        } else {
            _stateSearchFieldLiveData.postValue(SearchFieldState.SearchTextEntered)
        }
        if (lastSearchText == changedText) {
            return
        }

        this.lastSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }



    fun refreshSearch (newSearchText: String){
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(newSearchText) }

        val postTime = SystemClock.uptimeMillis() + CLICK_DEBOUNCE_DELAY

        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN,postTime)
    }

    fun searchChangeFocus(hasFocus:Boolean,text:String){
        val historyTrack = searchInteractor.getHistory()
        if (hasFocus && text.isEmpty() && historyTrack.isNotEmpty()){
            renderState(SearchState.History(track = historyTrack))
        }
    }

    fun clearHistory(){
        searchInteractor.clearHistory()
        renderState(SearchState.Tracks(tracks = emptyList()))
    }

    fun openTrack (track: Track){
        searchInteractor.addTrack(track)
    }
    @SuppressLint("SuspiciousIndentation")
    fun searchRequest(newSearchText: String) {
        if (newSearchText.isEmpty()) return
        renderState(SearchState.Loading)
        searchInteractor.searchTracks(newSearchText, object : SearchInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                val tracks = mutableListOf<Track>()
                if (foundTracks != null) {
                    tracks.addAll(foundTracks)
                }
                when {
                    errorMessage != null -> renderState(SearchState.CommunicationProblems)
                    tracks.isEmpty() -> renderState(SearchState.NothingFound)
                    else -> renderState(SearchState.Tracks(tracks))

                }

            }
        })
    }

    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                val gson = Gson()
                val networkClient  = RetrofitNetworkClient(context)
                val trackRepository = TrackRepositoryImpl(networkClient)
                val trackHistory = TrackHistoryImpl(context, gson)
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {

                    return SearchViewModel(searchInteractor = SearchInteractorImpl(trackHistory, trackRepository)) as T
                }
            }
    }
}