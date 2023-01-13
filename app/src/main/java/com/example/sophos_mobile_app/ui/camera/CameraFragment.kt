package com.example.sophos_mobile_app.ui.camera


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.BuildConfig
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val cameraViewModel: CameraViewModel by activityViewModels()

    private var cameraPhotoFilePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //observeViewModel()
        openSomeActivityForResult()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    cameraPhotoFilePath?.let { uri ->
                        val imageBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                        cameraViewModel.createImage64Photo(imageBitmap)
                        showMessage(getString(R.string.image_uploaded))
                        findNavController().popBackStack(R.id.sendDocumentsFragmentDestination, false)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else if(result.resultCode == Activity.RESULT_CANCELED){
                showMessage(getString(R.string.no_image_attached))
                findNavController().popBackStack(R.id.sendDocumentsFragmentDestination, false)
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            absolutePath
        }
    }

    private fun openSomeActivityForResult() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider", it)
                cameraPhotoFilePath = photoURI
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        }
        resultLauncher.launch(intent)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}