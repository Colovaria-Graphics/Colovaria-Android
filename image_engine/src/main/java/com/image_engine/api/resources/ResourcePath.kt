package com.image_engine.api.resources

import android.content.Context
import android.net.Uri
import java.io.FileOutputStream
import java.io.OutputStream

sealed class ResourcePath {
    fun outputStream(context: Context) : OutputStream {
        return when (this) {
            is AssetResourcePath -> error("Can't write to asset")
            is InternalStorageResourcePath -> FileOutputStream(path)
            is ExternalStorageResourcePath -> context.contentResolver.openOutputStream(uri)!!
            is WebResourcePath -> error("Can't write to web")
            is AndroidResResourcePath -> error("Can't write to res")
        }
    }

    // This is an unique identifier that represent the resource path
    fun pathId() : String {
        return this::class.simpleName + "_" + when (this) {
            is AssetResourcePath -> path
            is InternalStorageResourcePath -> path
            is ExternalStorageResourcePath -> uri.toString()
            is WebResourcePath -> url
            is AndroidResResourcePath -> id.toString()
        }
    }
}

data class AssetResourcePath(
    val path: String
) : ResourcePath()

data class InternalStorageResourcePath(
    val path: String,
) : ResourcePath()

data class ExternalStorageResourcePath(
    val uri: Uri,
) : ResourcePath()

data class WebResourcePath(
    val url: String,
) : ResourcePath()

data class AndroidResResourcePath(
    val id: Int,
) : ResourcePath()
