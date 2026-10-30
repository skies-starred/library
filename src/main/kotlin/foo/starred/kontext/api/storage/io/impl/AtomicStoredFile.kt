package foo.starred.kontext.api.storage.io.impl

import foo.starred.kontext.api.storage.io.base.AbstractStoredFile
import foo.starred.kontext.util.safely
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class AtomicStoredFile(override val file: File) : AbstractStoredFile {
    val backup: File = File(file.parentFile, "${file.name}.bak")
    val temp: File = File(file.parentFile, "${file.name}.tmp")

    override fun read(): String? {
        if (file.exists() && file.length() > 0) {
            safely {
                return file.readText()
            }
        }

        if (backup.exists() && backup.length() > 0) {
            safely {
                return backup.readText()
            }
        }

        return null
    }

    override fun write(content: String) {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        temp.writeText(content)

        if (file.exists()) {
            safely {
                Files.copy(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        }

        try {
            Files.move(temp.toPath(), file.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
        } catch (_: Exception) {
            Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }

    override fun exists(): Boolean {
        return file.exists() || backup.exists()
    }

    override fun delete(): Boolean {
        var success = true

        if (file.exists()) {
            success = file.delete() && success
        }

        if (backup.exists()) {
            success = backup.delete() && success
        }

        if (temp.exists()) {
            success = temp.delete() && success
        }

        return success
    }
}

