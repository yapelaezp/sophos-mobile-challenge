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
class GalleryViewModel @Inject constructor(private val imageConverter: ImageConverterImpl): ViewModel(){

    companion object{
        const val IMAGE_MAX_WIDTH = 400
        const val IMAGE_MAX_HEIGHT = 350
    }

    private val _imageBase64 = MutableLiveData<String>()
    val imageBase64: LiveData<String>
        get() = _imageBase64

    fun setImage64Photo(imageBitmap: Bitmap){
        viewModelScope.launch {
            val bitmapResizedPhoto = imageConverter.resize(imageBitmap,
                IMAGE_MAX_WIDTH,
                IMAGE_MAX_HEIGHT
            )
            _imageBase64.value = imageConverter.bitmapToBase64(bitmapResizedPhoto)
        }
    }

}