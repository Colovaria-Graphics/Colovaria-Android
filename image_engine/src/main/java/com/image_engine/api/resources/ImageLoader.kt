package com.image_engine.api.resources

import android.graphics.Bitmap
import com.geometry.Size
import io.reactivex.rxjava3.core.Single

interface ImageLoader {
    fun load(resource: ResourcePath, targetSize: Size? = null): Single<Bitmap>

    fun size(resource: ResourcePath) : Single<Size>
}