package io.github.luteoos.gent.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object UriResolver {

    fun getFileFromUri(uri : Uri, cr: ContentResolver): File{
        val result = File.createTempFile("temp","gentree")
        val inStream = cr.openInputStream(uri)!!
        val outStream = FileOutputStream(result)
        inStream.copyTo(outStream)
        inStream.close()
        outStream.close()
        return result
    }
}