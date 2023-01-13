package com.example.sophos_mobile_app.utils

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy

interface ImageConverter {

    suspend fun imageProxyToBitmap(image: ImageProxy): Bitmap

    suspend fun bitmapToBase64(imageBitmap: Bitmap): String

    suspend fun base64ToBitmap(imageBase64: String): Bitmap

    suspend fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap

}