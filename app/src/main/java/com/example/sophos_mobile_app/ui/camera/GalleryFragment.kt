package com.example.sophos_mobile_app.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openGallery()
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result != null) {
                val uri: Uri? = result.data?.data
                try {
                    if (uri != null) {
                        val imageBitmap =
                            MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                uri
                            )
                        galleryViewModel.setImage64Photo(imageBitmap)
                        showMessage(getString(R.string.image_uploaded))
                        findNavController().popBackStack(R.id.sendDocumentsFragmentDestination, false)
                    } else {
                        showMessage(getString(R.string.no_image_attached))
                        findNavController().popBackStack(R.id.sendDocumentsFragmentDestination, false)
                    }
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