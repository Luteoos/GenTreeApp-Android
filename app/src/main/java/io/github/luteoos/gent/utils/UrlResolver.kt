package io.github.luteoos.gent.utils

import org.jetbrains.anko.doAsync
import java.net.URL

object UrlResolver {

    /**
     * getFileFromUrl(url, f) { f -> ...}
     */

    fun getFileFromUrl(address: String, callback: (CustomFile) -> Unit){
        val url = URL(address)

        var byteArray: ByteArray
        doAsync{
            byteArray = url.readBytes()
            callback(CustomFile(url.file, byteArray , byteArray.size))
        }
    }
}

class CustomFile(val file: String?,
                 val content: ByteArray,
                 val size: Int?)