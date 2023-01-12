package com.example.sophos_mobile_app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

interface ImageConverter {

    suspend fun imageProxyToBitmap(image: ImageProxy): Bitmap

    suspend fun bitmapToBase64(imageBitmap: Bitmap): String

    suspend fun base64ToBitmap(imageBase64: String): Bitmap

    suspend fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap

}