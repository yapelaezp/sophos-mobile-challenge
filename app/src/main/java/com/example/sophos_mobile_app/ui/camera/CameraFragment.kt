package com.example.sophos_mobile_app.ui.camera

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private val cameraViewModel: CameraViewModel by viewModels()
    private lateinit var outputDirectory: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        startCamera()
        setListeners()
        observeViewModel()
        outputDirectory = getOutputDirectory()
        return binding.root
    }

    private fun observeViewModel() {
        cameraViewModel.photoBase64.observe(viewLifecycleOwner){ base64Photo ->
            showMessage(getString(R.string.photo_saved))
            val action = CameraFragmentDirections.actionCameraFragmentToSendDocumentsFragmentDestination(base64Photo)
            findNavController().navigate(action)
        }
    }

    private fun setListeners() {
        binding.ibTakePhoto.setOnClickListener {
            takePhoto()
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else requireActivity().filesDir
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.pvCameraPreview.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder().build()

            val camSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, camSelector, preview, imageCapture
                )
            }catch (e: Exception){

            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    super.onCaptureSuccess(imageProxy)
                    try {
                        cameraViewModel.getBase64Photo(imageProxy)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}