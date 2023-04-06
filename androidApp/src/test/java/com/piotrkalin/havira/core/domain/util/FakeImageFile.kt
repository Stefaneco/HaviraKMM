package com.piotrkalin.havira.core.domain.util

class FakeImageFile() : IImageFile {
    override fun toResizedByteArray(sizeX: Int, sizeY: Int): ByteArray = ByteArray(0)

    override fun toByteArray(): ByteArray =ByteArray(0)
}