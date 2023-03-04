package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import kotlin.properties.Delegates.notNull


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SAVED_DATA = "SAVED_DATA"
    }

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


        val track1 = Track(
            resources.getString(R.string.nameTrack1),
            resources.getString(R.string.artistTrack1),
            resources.getString(R.string.timeTrack1),
            resources.getString(R.string.imageUrlTrack1)
        )
        val track2 = Track(
            resources.getString(R.string.nameTrack2),
            resources.getString(R.string.artistTrack2),
            resources.getString(R.string.timeTrack2),
            resources.getString(R.string.imageUrlTrack2)
        )
        val track3 = Track(
            resources.getString(R.string.nameTrack3),
            resources.getString(R.string.artistTrack3),
            resources.getString(R.string.timeTrack3),
            resources.getString(R.string.imageUrlTrack3)
        )
        val track4 = Track(
            resources.getString(R.string.nameTrack4),
            resources.getString(R.string.artistTrack4),
            resources.getString(R.string.timeTrack4),
            resources.getString(R.string.imageUrlTrack4)
        )
        val track5 = Track(
            resources.getString(R.string.nameTrack5),
            resources.getString(R.string.artistTrack5),
            resources.getString(R.string.timeTrack5),
            resources.getString(R.string.imageUrlTrack5)
        )
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            recyclerView.adapter = adapter

            with(adapter) {
                addTrack(track1)
                addTrack(track2)
                addTrack(track3)
                addTrack(track4)
                addTrack(track5)
            }
        }


        if (savedInstanceState == null) {
            textBox = ""
        } else {
            textBox = savedInstanceState.getString(SAVED_DATA).toString()
        }
        renderState()

        val text = binding.inputEditText.text

        binding.searchBack.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
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
        binding.inputEditText.setText(textBox.toString())
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
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

