package com.example.sophos_mobile_app.ui.camera

import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.utils.ImageConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val imageConverter: ImageConverter): ViewModel() {

    companion object{
        const val PHOTO_MAX_WIDTH = 400
        const val PHOTO_MAX_HEIGHT = 350
    }

    private val _photoBase64 = MutableLiveData<String>()
    val photoBase64: LiveData<String>
    get() = _photoBase64

    fun getBase64Photo(imageProxy: ImageProxy){
        viewModelScope.launch {
            val bitmapPhoto = imageConverter.imageProxyToBitmap(imageProxy)
            val bitmapResizedPhoto = imageConverter.resize(bitmapPhoto, PHOTO_MAX_WIDTH, PHOTO_MAX_HEIGHT)
            _photoBase64.value = imageConverter.bitmapToBase64(bitmapResizedPhoto)
        }
    }

}