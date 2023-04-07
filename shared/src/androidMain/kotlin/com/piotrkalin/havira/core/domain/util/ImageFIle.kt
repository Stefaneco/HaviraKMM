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

        // Calculate the size and position for cropping the image into a square
        val cropSize = minOf(bitmap.width, bitmap.height)
        val offsetX = (bitmap.width - cropSize) / 2
        val offsetY = (bitmap.height - cropSize) / 2

        // Crop the bitmap to a square
        val croppedBitmap = Bitmap.createBitmap(bitmap, offsetX, offsetY, cropSize, cropSize)

        // Calculate the scale factor and the new width and height
        val scaleFactor = minOf(sizeX.toFloat() / croppedBitmap.width, sizeY.toFloat() / croppedBitmap.height)
        val newWidth = (croppedBitmap.width * scaleFactor).toInt()
        val newHeight = (croppedBitmap.height * scaleFactor).toInt()

        // Resize the cropped bitmap
        val resizedBitmap = Bitmap.createScaledBitmap(croppedBitmap, newWidth, newHeight, true)

        // Convert the resized bitmap back to a byteArray
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }
}
