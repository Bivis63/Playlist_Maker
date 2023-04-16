package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates.notNull


class SearchActivity : AppCompatActivity(), TrackAdapter.OnTrackClickListener {

    companion object {
        const val SAVED_DATA = "saved_data"
    }

    enum class PlaceHolder {
        SUCCESS,
        ERROR,
        EMPTY,
    }

    private val songBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(songBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackList = ArrayList<Track>()
    private val itunesService = retrofit.create(TrackApi::class.java)
    private val adapter = TrackAdapter(this)
    private val historyAdapter = TrackAdapter(this)
    private lateinit var searchHistory: SearchHistory
    private var textBox by notNull<String>()


    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_DATA, textBox)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        textBox = savedInstanceState?.getString(SAVED_DATA).orEmpty()

        renderState()

        val text = binding.inputEditText.text



        adapter.tracksList = trackList
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)


        val sharedPreferences = getSharedPreferences(SearchHistory.TRACKS_HISTORY, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        binding.recyclerTrackHistory.adapter = historyAdapter
        binding.recyclerTrackHistory.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)



        binding.clearHistory.setOnClickListener {
            searchHistory.ClearSearchHistoryList()
            historyAdapter.notifyDataSetChanged()
        }

        checkList()

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistory.isVisible = hasFocus && binding.inputEditText.text.isEmpty()
            historyAdapter.tracksList = searchHistory.load()
            historyAdapter.notifyDataSetChanged()
        }



        binding.buttonUpdate.setOnClickListener {
            getTrackList()
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
            ) {
                getTrackList()
                true
            }
            false
        }


        binding.searchBack.setOnClickListener {
            finish()

        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            adapter.removeTrackList()
            showPlaceHolder(PlaceHolder.SUCCESS)
            it.hideKeyboard()

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchHistoryTrackList = searchHistory.load()
                binding.clearIcon.visibility = clearButtonVisibility(s)
                binding.searchHistory.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && searchHistoryTrackList.isNotEmpty()) {
                        adapter.removeTrackList()
                        historyAdapter.notifyDataSetChanged()
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                historyAdapter.tracksList = searchHistory.load()
                showPlaceHolder(PlaceHolder.SUCCESS)


            }

            override fun afterTextChanged(s: Editable?) {
                text.toString()
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun renderState() {
        binding.inputEditText.setText(textBox)
    }


    fun getTrackList() {
        if (binding.inputEditText.text.isNotEmpty()) {
            itunesService.search(binding.inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {

                            if (response.body()?.results?.isNotEmpty() == true) {

                                adapter.tracksList =
                                    (response.body()?.results as ArrayList<Track>?)!!
                                showPlaceHolder(PlaceHolder.SUCCESS)
                            } else {
                                showPlaceHolder(PlaceHolder.EMPTY)
                            }
                        } else {
                            showPlaceHolder(PlaceHolder.ERROR)

                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showPlaceHolder(PlaceHolder.ERROR)
                    }
                })
        }
    }

    private fun showPlaceHolder(placeholder: PlaceHolder) {
        when (placeholder) {
            PlaceHolder.EMPTY -> {
                adapter.removeTrackList()
                binding.imageHolder.visibility = View.VISIBLE
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.placeholderMessage.text = getString(R.string.nothing_found)
                binding.imageHolderNoInternet.visibility = View.GONE
                binding.placeholderMessageNoInternet.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
            }
            PlaceHolder.ERROR -> {
                adapter.removeTrackList()
                binding.imageHolderNoInternet.visibility = View.VISIBLE
                binding.placeholderMessageNoInternet.visibility = View.VISIBLE
                binding.buttonUpdate.visibility = View.VISIBLE
                binding.placeholderMessage.visibility = View.VISIBLE
                binding.imageHolder.visibility = View.GONE
                binding.placeholderMessage.text = getString(R.string.Communication_problems)
                binding.placeholderMessageNoInternet.text = getString(R.string.Download_failed)

            }
            else -> {
                binding.imageHolderNoInternet.visibility = View.GONE
                binding.imageHolder.visibility = View.GONE
                binding.placeholderMessage.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
                binding.placeholderMessageNoInternet.visibility = View.GONE

            }
        }
    }

    override fun onClick(track: Track) {
//        Toast.makeText(this, " Добавили в историю ${track.artistName}", Toast.LENGTH_LONG).show()
        startActivity( Intent(this, AudioPlayerActivity::class.java).apply {
            putExtra("item",track)
        })
        searchHistory.addTrack(track)

        historyAdapter.notifyDataSetChanged()

    }


    private fun checkList() {
        historyAdapter.tracksList = searchHistory.load()
        if (historyAdapter.tracksList.isNotEmpty()) {
            binding.searchHistory.isVisible = true
        }
        historyAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        if (binding.inputEditText.text.isEmpty()){
            checkList()
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}




