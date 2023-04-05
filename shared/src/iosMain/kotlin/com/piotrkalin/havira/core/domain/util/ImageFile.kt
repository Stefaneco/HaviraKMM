package com.piotrkalin.havira.core.domain.util

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

//modified version of this medium post
//https://nrobir.medium.com/uploading-images-in-kotlin-multiplatform-ecf87e866505

//actual typealias ImageFile = UIImage
actual typealias ImageFile = UIImageWrapper

private fun NSData.toByteArray(): ByteArray = ByteArray(length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), bytes, length)
    }
}

class UIImageWrapper(val uiImage: UIImage) : IImageFile {

    override fun toResizedByteArray(sizeX: Int, sizeY: Int): ByteArray {
        TODO("Not yet implemented")
    }

    override fun toByteArray(): ByteArray = UIImagePNGRepresentation(uiImage)?.toByteArray() ?: emptyArray<Byte>().toByteArray()

}
