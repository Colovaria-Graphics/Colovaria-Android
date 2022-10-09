package com.glide_image_loader

import android.content.Context
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.colovaria.image_engine.api.resources.ResourcePath
import java.io.InputStream

class ResourcePathLoaderFactory(
    private val context: Context,
) : ModelLoaderFactory<ResourcePath, InputStream> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ResourcePath, InputStream> {
        return  ResourcePathLoader(context)
    }

    override fun teardown() {}
}