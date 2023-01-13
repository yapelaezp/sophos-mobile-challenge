package com.example.sophos_mobile_app.ui.camera

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.utils.ImageConverterImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val imageConverter: ImageConverterImpl): ViewModel() {

    companion object;

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