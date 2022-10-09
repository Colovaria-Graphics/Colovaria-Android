package com.glide_image_loader

import android.content.Context
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.colovaria.geometry.Size
import com.colovaria.image_engine.api.resources.ResourcePath
import java.io.InputStream

@GlideModule
class GlideWithImageLoader : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(ResourcePath::class.java, InputStream::class.java,
            ResourcePathLoaderFactory(context)
        )
        registry.prepend(InputStream::class.java, BitmapFactory.Options::class.java,
            BitmapSizeDecoder()
        )
        registry.register(BitmapFactory.Options::class.java, Size::class.java,
            OptionsSizeResourceTranscoder()
        )
    }
}