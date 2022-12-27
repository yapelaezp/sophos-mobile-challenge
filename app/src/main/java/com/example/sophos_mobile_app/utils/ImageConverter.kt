package com.example.sophos_mobile_app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import javax.inject.Inject

class ImageConverter @Inject constructor() {

    suspend fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        return withContext(Dispatchers.IO){
            val planeProxy = image.planes[0]
            val buffer: ByteBuffer = planeProxy.buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    suspend fun bitmapToBase64(imageBitmap: Bitmap): String {
        return withContext(Dispatchers.IO){
            val stream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageBytes = stream.toByteArray()
            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }
    }

    suspend fun base64ToBitmap(imageBase64: String): Bitmap{
        return withContext(Dispatchers.IO) {
            val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
            val bitmapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            bitmapImage
        }
    }

    suspend fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        return withContext(Dispatchers.IO){
            var image = image
            if (maxHeight > 0 && maxWidth > 0) {
                val width = image.width
                val height = image.height
                val ratioBitmap = width.toFloat() / height.toFloat()
                val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
                var finalWidth = maxWidth
                var finalHeight = maxHeight
                if (ratioMax > ratioBitmap) {
                    finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
                } else {
                    finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
                image
            } else {
                image
            }
        }
    }
}