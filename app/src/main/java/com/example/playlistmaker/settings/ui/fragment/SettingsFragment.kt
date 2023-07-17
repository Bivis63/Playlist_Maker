package com.example.playlistmaker.settings.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingViewModel
import com.example.playlistmaker.sharing.domain.model.EmailData
import com.example.playlistmaker.util.App
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding:FragmentSettingsBinding
    private val viewModel by viewModel<SettingViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isDarkThemeEnable.observe(this, Observer { isDarkThemeEnable ->
            binding.themeSwitcher.isChecked = isDarkThemeEnable
        })

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            viewModel.setTheme(cheked)
            (requireContext().applicationContext as App).switchTheme(cheked)

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