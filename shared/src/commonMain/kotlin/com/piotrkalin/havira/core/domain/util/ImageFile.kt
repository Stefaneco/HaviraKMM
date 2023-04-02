package com.piotrkalin.havira.core.domain.util

//https://nrobir.medium.com/uploading-images-in-kotlin-multiplatform-ecf87e866505

expect class ImageFile

expect fun ImageFile.toByteArray() : ByteArray

//----

expect fun ImageFile.toResizedByteArray(sizeX: Int, sizeY : Int) : ByteArray