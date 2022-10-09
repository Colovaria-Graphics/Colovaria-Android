# Image Engine Usage:
| Section | Description |
|--|--|
| [Dependencies](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#dependencies) |  |
| [Basic Usage](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#basic-usage) | Basic overview of the library usage |
| [Image Loader](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#image-loader) | Information about Image Loader concept |


## Dependencies:
```kotlin
// For image engine:
implementation "io.github.colovaria-graphics:image_engine:+"
```

Latest version - [![][image_engine_badge img]][image_engine_badge]

## Basic Usage:
In order to live-render a frame to the screen, the first object we will need to create is `ImageEngine`.
`ImageEngine` takes `Context` and `ImageLoader` provider:
```kotlin
val imageEngine = ImageEngine(requireContext(), imageLoaderProvider)
```
(For information about `ImageLoader` and `ImageLoader` provider for see [Image Loader section](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#image-loader)).

Next step is to attach a `SurfaceView` or `TextureView` to the engine, and call `start`:
```kotlin
surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {}

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {
        imageEngine.setTargetSurface(surfaceHolder.surface)
        imageEngine.start()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
}
```

`imageEngine.start()` will cause the image engine to start rendering frames to the surface we have attached using `imageEngine.setTargetSurface()`.


Now we can send `Frame`'s to the engine using `imageEngine.setFrame()`.


### Image loader:
In order to be very generic, `ImageEngine` dosen't implement image loading realted logic, so the library user must implement it itself (by implementeing the interace `ImageLoader`) and passing it to the engine.
See [here]() glide implementation for example

[image_engine_badge]:https://search.maven.org/artifact/io.github.colovaria-graphics/image_engine  
[image_engine_badge img]:https://img.shields.io/maven-central/v/io.github.colovaria-graphics/image_engine.svg?label=
