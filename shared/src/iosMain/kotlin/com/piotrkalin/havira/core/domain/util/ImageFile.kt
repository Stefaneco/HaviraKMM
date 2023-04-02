package com.piotrkalin.havira.core.domain.util

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

//https://nrobir.medium.com/uploading-images-in-kotlin-multiplatform-ecf87e866505

actual typealias ImageFile = UIImage

actual fun ImageFile.toByteArray() = UIImagePNGRepresentation(this)?.toByteArray() ?: emptyArray<Byte>().toByteArray()

actual fun ImageFile.toResizedByteArray(sizeX: Int, sizeY: Int) : ByteArray = UIImagePNGRepresentation(this)?.toByteArray() ?: emptyArray<Byte>().toByteArray()

private fun NSData.toByteArray(): ByteArray = ByteArray(length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), bytes, length)
    }
}
