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
import com.example.playlistmaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.FileOutputStream


class NewPlayListFragment : Fragment() {

    private lateinit var binding: FragmentNewPlayListBinding
    private var showDialog: Boolean = false


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
                }else{
                    fragmentManager?.popBackStack()
                }
            }
        }
        onBackPressedDispatcher.addCallback(callback)

        binding.createButton.setOnClickListener {
            Toast.makeText(requireContext(),"Плейлист ${binding.textInputEditText.text} создан",Toast.LENGTH_SHORT).show()
            fragmentManager?.popBackStack()
        }

        binding.backToMedia.setOnClickListener {
            if (showDialog) {
                openDialog()
            } else {
                fragmentManager?.popBackStack()
            }
        }
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
        showDialog=true
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
                fragmentManager?.popBackStack()
            }
            .show()
    }
}