package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.player.ui.activity.ITEM
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchFieldState
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.*
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity(), TrackAdapter.OnTrackClickListener {


    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val viewModel by viewModel<SearchViewModel>()
    private val trackList = ArrayList<Track>()
    private val adapter = TrackAdapter(this)
    private val historyAdapter = TrackAdapter(this)
    private var textWatcher: TextWatcher? = null


    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        adapter.tracksList = trackList
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)



        binding.recyclerTrackHistory.adapter = historyAdapter
        binding.recyclerTrackHistory.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)




        binding.searchBack.setOnClickListener {
            finish()

        }
        binding.clearIcon.setOnClickListener {
            viewModel.clearSearchText()
            hidePlaceHolder()


        }
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchRequest(binding.inputEditText.text.toString())
                true
            } else {
                false
            }

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        simpleTextWatcher?.let { binding.inputEditText.addTextChangedListener(it) }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchChangeFocus(hasFocus, binding.inputEditText.text.toString())
        }
        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
        viewModel.stateLiveData.observe(this) {
            render(it)
        }
        viewModel.stateSearchFieldLiveData.observe(this) {
            updateSearchTextField(it)
        }
        binding.buttonUpdate.setOnClickListener {
            viewModel.searchRequest(binding.inputEditText.text.toString())
            hidePlaceHolder()
        }

    }

    private fun updateSearchTextField(state: SearchFieldState) {
        when (state) {
            is SearchFieldState.SearchTextEmpty -> {
                binding.clearIcon.visibility = View.GONE
                if (state.needClear) {
                    binding.inputEditText.setText("")
                    binding.inputEditText.clearFocus()
                    hideKeyboard()
                }
            }

            SearchFieldState.SearchTextEntered -> {
                binding.clearIcon.visibility = View.VISIBLE
            }
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Tracks -> showTrack(state.tracks)
            is SearchState.History -> showHistory(state.track)
            is SearchState.Loading -> showLoading()
            is SearchState.CommunicationProblems -> showNoInternet()
            is SearchState.NothingFound -> showNotFound()
        }
    }

    private fun hidePlaceHolder() {
        binding.placeholderMessage.visibility = View.GONE
        binding.placeholderMessageNoInternet.visibility = View.GONE
        binding.imageHolderNoInternet.visibility = View.GONE
        binding.imageHolder.visibility = View.GONE
        binding.buttonUpdate.visibility = View.GONE

    }

    private fun showTrack(tracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.placeholderMessageNoInternet.visibility = View.GONE
        binding.recyclerTrackHistory.visibility = View.VISIBLE
        binding.searchHistory.visibility = View.GONE

        historyAdapter.tracksList.clear()
        historyAdapter.notifyDataSetChanged()
        adapter.tracksList.clear()
        adapter.tracksList.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showHistory(tracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.placeholderMessageNoInternet.visibility = View.GONE
        binding.recyclerTrackHistory.visibility = View.VISIBLE
        binding.searchHistory.visibility = View.VISIBLE

        adapter.tracksList.clear()
        adapter.notifyDataSetChanged()
        historyAdapter.tracksList.clear()
        historyAdapter.tracksList.addAll(tracks)
        historyAdapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        adapter.tracksList.clear()
        adapter.notifyDataSetChanged()
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerTrackHistory.visibility = View.GONE
        binding.searchHistory.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.placeholderMessageNoInternet.visibility = View.GONE

    }

    private fun showNoInternet() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerTrackHistory.visibility = View.GONE
        binding.searchHistory.visibility = View.GONE
        binding.placeholderMessageNoInternet.visibility = View.VISIBLE
        binding.imageHolderNoInternet.visibility = View.VISIBLE
        binding.placeholderMessage.text = getString(R.string.Communication_problems)
        binding.placeholderMessageNoInternet.text = getString(R.string.Download_failed)
        binding.buttonUpdate.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
    }

    private fun showNotFound() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerTrackHistory.visibility = View.GONE
        binding.searchHistory.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderMessageNoInternet.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderMessage.text = getString(R.string.nothing_found)
        binding.imageHolder.visibility = View.VISIBLE

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            startActivity(Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra(ITEM, track)
            })
            viewModel.openTrack(track)

            historyAdapter.notifyDataSetChanged()
        }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}