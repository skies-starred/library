package foo.starred.kontext.api.storage.io.base

import java.io.File

interface AbstractStoredFile {
    val file: File

    fun read(): String?
    fun write(content: String)
    fun exists(): Boolean
    fun delete(): Boolean
}

