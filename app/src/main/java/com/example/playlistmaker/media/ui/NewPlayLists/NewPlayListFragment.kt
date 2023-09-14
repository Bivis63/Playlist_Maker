package com.example.playlistmaker.media.ui.NewPlayLists


import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.ui.viewModel.NewPlayListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class NewPlayListFragment : Fragment() {

    private lateinit var binding: FragmentNewPlayListBinding
    private var showDialog: Boolean = false
    private val viewModel by viewModel<NewPlayListViewModel>()
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideBottomNavigation()

        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    selectedImageUri= viewModel.saveImageToPrivateStorage(uri, requireContext().applicationContext)
                    binding.imageView.setImageURI(uri)
                    showDialog = true
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.textInputEditText.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty()) {
                val color = ContextCompat.getColor(requireContext(), R.color.blue)
                showDialog = true
                binding.createButton.setBackgroundColor(color)
                binding.createButton.isEnabled = true
            } else {
                val color = ContextCompat.getColor(requireContext(), R.color.grey)
                binding.createButton.setBackgroundColor(color)
                binding.createButton.isEnabled = false
            }
        }

        binding.textInputEditTextDescription.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty()) {
                showDialog = true
            }
        }

        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (showDialog) {
                    openDialog()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
        onBackPressedDispatcher.addCallback(callback)

        binding.createButton.setOnClickListener {
            addPlaylist(selectedImageUri)
            Toast.makeText(
                requireContext(),
                "${getString(R.string.playlist)} ${binding.textInputEditText.text} ${getString(R.string.created)}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        }

        binding.backToMedia.setOnClickListener {
            if (showDialog) {
                openDialog()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun addPlaylist(imageUri: Uri?) {
        val name = binding.textInputEditText.text.toString()
        val description = binding.textInputEditTextDescription.text.toString()
        val imagePath = viewModel.generateImageTitle()

        viewModel.insertPlayList(
            PlayListsModels(
                id = 0,
                name = name,
                description = description,
                imagePath = imagePath,
                trackIds = "",
                trackCount = 0,
                tracks = arrayListOf(),
                imageUri = imageUri?.toString()
            )
        )
    }

    private fun openDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.finish_creating_a_playlist))
            .setMessage(getString(R.string.all_unsaved_data_will_be_lost))
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(getString(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).openBottomNavigation()
    }
}