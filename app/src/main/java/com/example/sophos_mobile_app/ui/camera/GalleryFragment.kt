package com.example.sophos_mobile_app.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val galleryViewModel: CameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openGallery()
        observeViewModel()
    }

    private fun observeViewModel() {
        galleryViewModel.imageBase64.observe(viewLifecycleOwner) { imageBase64 ->
            showMessage(getString(R.string.image_saved))
            val action =
                GalleryFragmentDirections.actionGalleryFragmentToSendDocumentsFragmentDestination(
                    imageBase64
                )
            findNavController().navigate(action)
        }
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result != null) {
                val uri: Uri? = result.data?.data
                try {
                    val imageBitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    galleryViewModel.getImage64Photo(imageBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        imagePickerActivityResult.launch(galleryIntent)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}