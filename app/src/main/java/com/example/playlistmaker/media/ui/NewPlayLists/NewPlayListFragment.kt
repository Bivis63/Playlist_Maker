package com.example.playlistmaker.media.ui.NewPlayLists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import java.io.File
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.media.data.db_for_playlists.PlaylistEntity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.ui.PlayListsFragment
import com.example.playlistmaker.media.ui.viewModel.NewPlayListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.FileOutputStream


class NewPlayListFragment : Fragment() {

    private lateinit var binding: FragmentNewPlayListBinding
    private var showDialog: Boolean = false
    private val viewModel by viewModel<NewPlayListViewModel>()
    var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imageView.setImageURI(uri)
                    selectedImageUri=uri
                    saveImageToPrivateStorage(uri)
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
            Toast.makeText(context!!.applicationContext, "yes", Toast.LENGTH_SHORT).show()
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
            addToDB(selectedImageUri)
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.textInputEditText.text} создан",
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

    private fun addToDB(imageUri: Uri?) {
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
                imageUri = imageUri?.toString()
            )
        )
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        showDialog = true
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun openDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, which ->
            }
            .setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

}