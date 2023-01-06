package com.example.sophos_mobile_app.ui.camera

import android.graphics.Bitmap
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

    private val _imageBase64 = MutableLiveData<String>()
    val imageBase64: LiveData<String>
        get() = _imageBase64

    fun createImage64Photo(imageBitmap: Bitmap){
        viewModelScope.launch {
            val bitmapResizedPhoto = imageConverter.resize(imageBitmap,
                GalleryViewModel.IMAGE_MAX_WIDTH,
                GalleryViewModel.IMAGE_MAX_HEIGHT
            )
            _imageBase64.value = imageConverter.bitmapToBase64(bitmapResizedPhoto)
        }
    }

}