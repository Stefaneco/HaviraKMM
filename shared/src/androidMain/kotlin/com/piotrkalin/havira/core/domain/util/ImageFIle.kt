package com.piotrkalin.havira.core.domain.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

//modified version of this medium post
//https://nrobir.medium.com/uploading-images-in-kotlin-multiplatform-ecf87e866505

actual typealias ImageFile = ImageUri

class ImageUri(val uri: Uri, val contentResolver: ContentResolver) : IImageFile {
    override fun toByteArray() = contentResolver.openInputStream(uri)?.use {
        it.readBytes()
    } ?: throw IllegalStateException("Couldn't open inputStream $uri")

    override fun toResizedByteArray(sizeX: Int, sizeY: Int): ByteArray {
        val byteArray = contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        } ?: throw IllegalStateException("Couldn't open inputStream $uri")
        // Decode the byteArray into a bitmap
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        // Calculate the scale factor and the new width and height
        val scaleFactor = minOf(sizeX.toFloat() / bitmap.width, sizeY.toFloat() / bitmap.height)
        val newWidth = (bitmap.width * scaleFactor).toInt()
        val newHeight = (bitmap.height * scaleFactor).toInt()
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        // Convert the cropped bitmap back to a byteArray
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }
}
