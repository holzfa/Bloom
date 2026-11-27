package io.holzfa.bloom.debug

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import java.io.File

// FileKit uses the XDG desktop portal on Linux, which gives the native (themed) file chooser
object FileDialogs {
    suspend fun open(): File? = FileKit.openFilePicker(type = FileKitType.File(setOf("vu")))?.file

    suspend fun save(suggestedName: String): File? {
        val file = FileKit.openFileSaver(suggestedName = suggestedName, defaultExtension = "vu")?.file ?: return null
        return if (file.extension.isEmpty()) File(file.parentFile, file.name + ".vu") else file
    }
}
