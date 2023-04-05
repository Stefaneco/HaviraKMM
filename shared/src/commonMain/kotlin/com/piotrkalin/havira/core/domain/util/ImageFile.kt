package com.piotrkalin.havira.core.domain.util

//modified version of this medium post
//https://nrobir.medium.com/uploading-images-in-kotlin-multiplatform-ecf87e866505

interface IImageFile {
    fun toResizedByteArray(sizeX: Int, sizeY: Int): ByteArray
    fun toByteArray() : ByteArray
}

expect class ImageFile : IImageFile