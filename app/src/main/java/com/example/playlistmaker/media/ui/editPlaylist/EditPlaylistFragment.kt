package com.example.playlistmaker.media.ui.editPlaylist

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.domain.db.models.PlayListsModels
import com.example.playlistmaker.media.ui.NewPlayLists.NewPlayListFragment
import com.example.playlistmaker.media.ui.viewModel.EditPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditPlaylistFragment : NewPlayListFragment() {

    private var binding: FragmentEditPlaylistBinding? = null
    private var selectedImageUri: Uri? = null
    private val viewModel by viewModel<EditPlayListViewModel>()
    private var playList: PlayListsModels? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        playList = arguments?.getSerializable("playlistModel") as? PlayListsModels

        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher

        playList?.let { playlist ->
            binding?.textInputEditText?.setText(playlist.name)
            binding?.textInputEditTextDescription?.setText(playlist.description)

            if (!playlist.imageUri.isNullOrEmpty()) {
                val imageUri = Uri.parse(playlist.imageUri)
                selectedImageUri = imageUri
                binding?.imageView?.setImageURI(imageUri)
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.saveImageToPrivateStorage(uri, requireContext())
                    binding!!.imageView.setImageURI(uri)

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        viewModel.savedImageUri.observe(viewLifecycleOwner, Observer { savedUri ->
            selectedImageUri = savedUri
        })
        binding?.imageView?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                findNavController().navigateUp()
            }
        }
        onBackPressedDispatcher.addCallback(callback)

        binding?.createButton?.setOnClickListener {
            playList?.let { playlist ->
                addPlaylist(selectedImageUri, playlist)

                findNavController().navigateUp()
            }
        }

        checkInputText()

        binding?.backToMedia?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun addPlaylist(imageUri: Uri?, originalPlayList: PlayListsModels) {
        val name = binding?.textInputEditText?.text.toString()
        val description = binding?.textInputEditTextDescription?.text.toString()
        val imagePath = viewModel.generateImageTitle()

        viewModel.updatePlayList(
            PlayListsModels(
                id = originalPlayList.id,
                name = name,
                description = description,
                imagePath = imagePath,
                trackIds = originalPlayList.trackIds,
                trackCount = originalPlayList.trackCount,
                tracks = originalPlayList.tracks,
                imageUri = imageUri?.toString() ?: originalPlayList.imageUri
            )
        )
    }

    private fun checkInputText() {
        binding?.textInputEditText?.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty()) {
                val color = ContextCompat.getColor(requireContext(), R.color.blue)
                binding!!.createButton.setBackgroundColor(color)
                binding!!.createButton.isEnabled = true
            } else {
                val color = ContextCompat.getColor(requireContext(), R.color.grey)
                binding!!.createButton.setBackgroundColor(color)
                binding!!.createButton.isEnabled = false
            }
        }
    }
    private fun enablingTheSaveButton(){
        val color = ContextCompat.getColor(requireContext(), R.color.blue)
        binding!!.createButton.setBackgroundColor(color)
        binding!!.createButton.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        enablingTheSaveButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideBottomNavigation()
    }
}
