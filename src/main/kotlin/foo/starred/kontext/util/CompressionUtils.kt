package foo.starred.kontext.util

import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.Deflater
import java.util.zip.Inflater

fun String.compress(): String {
    return Base64.getEncoder().encodeToString(ByteArrayOutputStream().also { b ->
        val buffer = ByteArray(1024)
        val deflate = Deflater().apply {
            setInput(this@compress.toByteArray())
            finish()
        }

        while (!deflate.finished()) b.write(buffer, 0, deflate.deflate(buffer))
    }.toByteArray())
}

fun String.decompress(): String {
    return ByteArrayOutputStream().also { b ->
        val buffer = ByteArray(1024)
        val inflater = Inflater().apply {
            setInput(Base64.getDecoder().decode(this@decompress))
        }

        while (!inflater.finished()) b.write(buffer, 0, inflater.inflate(buffer))
    }.toString()
}
