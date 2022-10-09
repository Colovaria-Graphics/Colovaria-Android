package com.glide_image_loader

import android.graphics.BitmapFactory
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.colovaria.geometry.Size

class OptionsSizeResourceTranscoder : ResourceTranscoder<BitmapFactory.Options, Size> {
    override fun transcode(resource: Resource<BitmapFactory.Options>, options: Options) : Resource<Size> {
        return SimpleResource(resource.get().run { Size(outWidth, outHeight) })
    }
}