package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates.notNull


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SAVED_DATA = "SAVED_DATA"
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
    private val adapter = TrackAdapter()
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

        if (savedInstanceState == null) {
            textBox = ""
        } else {
            textBox = savedInstanceState.getString(SAVED_DATA).toString()
        }
        renderState()
        val text = binding.inputEditText.text


        adapter.tracksList = trackList
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

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
            it.hideKeyboard()

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
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

                                adapter.tracksList= (response.body()?.results as ArrayList<Track>?)!!
                                showPlaceHolder(PlaceHolder.SUCCESS)
                            }else{
                                showPlaceHolder(PlaceHolder.EMPTY)
                            }
                        }else{
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
                binding.placeholderMessage.visibility=View.VISIBLE
                binding.placeholderMessage.text = getString(R.string.nothing_found)
                binding.imageHolderNoInternet.visibility= View.GONE
                binding.placeholderMessageNoInternet.visibility=View.GONE
                binding.buttonUpdate.visibility=View.GONE
            }
            PlaceHolder.ERROR-> {
                adapter.removeTrackList()
                binding.imageHolderNoInternet.visibility= View.VISIBLE
                binding.placeholderMessageNoInternet.visibility=View.VISIBLE
                binding.buttonUpdate.visibility=View.VISIBLE
                binding.placeholderMessage.visibility=View.VISIBLE
                binding.imageHolder.visibility = View.GONE
                binding.placeholderMessage.text  = getString(R.string.Communication_problems)
                binding.placeholderMessageNoInternet.text = getString(R.string.Download_failed)

            }
            else ->{
                binding.imageHolderNoInternet.visibility= View.GONE
                binding.imageHolder.visibility = View.GONE
                binding.placeholderMessage.visibility=View.GONE
                binding.buttonUpdate.visibility=View.GONE
                binding.placeholderMessageNoInternet.visibility=View.GONE
            }
        }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

