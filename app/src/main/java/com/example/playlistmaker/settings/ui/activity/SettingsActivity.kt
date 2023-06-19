package com.example.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettings2Binding
import com.example.playlistmaker.settings.ui.viewmodel.SettingViewModel
import com.example.playlistmaker.sharing.domain.model.EmailData


class SettingsActivity : AppCompatActivity() {


    private lateinit var viewModel: SettingViewModel


    private val binding: ActivitySettings2Binding by lazy {
        ActivitySettings2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingViewModel.getSettingViewModelFactory()
        )[SettingViewModel::class.java]
        viewModel.isDarkThemeEnable.observe(this, Observer { isDarkThemeEnable ->
            binding.themeSwitcher.isChecked = isDarkThemeEnable
        })

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            viewModel.setTheme(cheked)
            (applicationContext as App).switchTheme(cheked)

        }

        binding.arrowBack.setOnClickListener {
            finish()
        }
        binding.support.setOnClickListener {
            viewModel.openSupport(
                EmailData(
                    email = resources.getString(R.string.Email),
                    message = resources.getString(R.string.messegeForDevelopers),
                    theme = resources.getString(R.string.thxForDevelopers)
                )
            )
        }
        binding.termsOfUse.setOnClickListener {
            viewModel.openTerms(resources.getString(R.string.offer))
        }
        binding.share.setOnClickListener {
            viewModel.shareApp(resources.getString(R.string.androidCourse))
        }

    }
}