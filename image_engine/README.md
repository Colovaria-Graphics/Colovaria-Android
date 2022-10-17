# Image Engine Usage:
| Section | Description |
|--|--|
| [Dependencies](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#dependencies) |  |
| [Basic Usage](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#basic-usage) | Basic overview of the library usage |
| [Image Loader](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#image-loader) | Information about Image Loader concept |
| [API](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#api) | Describes the API of the image engine |
| [Frame](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#frame) | Describes `Frame`, which is the API entry point |
| [Layer](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#layer) | Describes `Layer`, which is the building block of `Frame` |
| [Texture](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#texture) | Explanation about `TextureInstruction`  |
| [Blending](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#blending) | Explanation about `BlendingInstruction` |
| [Masking](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#mask) | Explanation about `MaskInstruction` |
| [Layers Examples](https://github.com/Colovaria-Graphics/Colovaria-Android/edit/main/image_engine/README.md#layers-examples) | Examples for layer creation |

## Dependencies:
```kotlin
// For image engine:
implementation "io.github.colovaria-graphics:image_engine:+"
implementation "io.github.colovaria-graphics:graphics:+"
implementation "io.github.colovaria-graphics:geometry:+"
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
See frame section.


### Image loader:
In order to be very generic, `ImageEngine` dosen't implement image loading realted logic, so the library user must implement it itself (by implementeing the interace `ImageLoader`) and passing it to the engine.

You can take [Glide](https://github.com/bumptech/glide) implementation from [here](https://github.com/Colovaria-Graphics/Colovaria-Android/tree/main/image_engine/smaples/glide_image_loader).

## Api
### Frame
`Frame` is high level object that represent (surprisingly) a frame on the screen, each frame is built up from layers, where each layer represent a texture that will be drawen on the screen.
So the first you should do is to create a `Frame`, and pass it the `ImageEngine` object.
```kotlin
val frame = Frame(...)
imageEngine.setFrame(frame)
```
You can call `setFrame` as many time as you want, by default `ImageEngine` draw 60 frames per second, so `setFrame` will cause the new frame to be shown on the screen in the next rendering time frame.

### Layer
Each `Layer` is is consist of 3 types of instructions, `texturing`, `blending` and `masking`:
1. `texturing` - describes the instructions need to be done in order to create a texture (will be explaind later).
2. `blending` - describes how the texture should be blanded (interact) with other layers.
3. `masking` - (Optional) describes how to apply a mask operation (cutting the texture).

#### Texture
Texture is a general name for the content of a visual object that could be shown on the screen.
In our case a texture might be an image, text, shape, etc...
In order to tell `ImageEngine` how to draw a certain texture, you needs to create a TextureInstruction,
There are a couple of builtin's textures types but it is possiable to create custom once too.

There are two types of texture instruction, drawer and processor instructions.
Drawers preduce textures only from instruction while Processors apply manipulation on a pre create texture.

The builtin's are types are:
1. TextInstruction - instruction for text drawing (such as text, size, font etc...)
2. ShapeInstruction - instruction for simple text drawing (Circle and Rectangle are supported).
3. ImageInstruction - instruction for drawing a given image (by path).
4. Gradient2DInstruction - instruction for drawing a 2d gradient.
5. HSVInstruction - instruction for that represent a HSV manipulation.

#### Blending
As said before, blending is a set of instruction that describes how to blend a given layer texture to the others layers textures.
Each BlendingInstruction object contains the following fields:
1. BlendMode - see more informations [here](https://en.wikipedia.org/wiki/Blend_modes), `ImageEngine`supports `Normal`, `Lighten`, `DarkDarkener` and `Multiply`.
2. Center - `Vector2f` the describes where to draw the texture, in absolut units (between 0 and 1).
3. Scale - Amount of scale that should be applied on the texture (1 mean original size).
4. Opacity - Value between 0 and 1 that describes how much the layer should be transperent, where 1 is not at all, and 0 is fully transperent.
5. AdjustMode - describes the relation between the blended layer size and the whole canvas size, see examples [here](https://thoughtbot.com/blog/android-imageview-scaletype-a-visual-guide), in android `ImageView` it typically called `scaleType`

#### Masking
Masking in an optional instruction that tells `ImageEngine` how to "cut" a given texture.
`MaskInstruction` is object that contains another texture that represent "where to cut" the layer texture.
Example in "Layers Examples" section.

### Layers Examples:
#### Text Layer
```kotlin
Layer(
    TextInstruction(
        text = "ThisIsALongTextWithoutSpaces",
        size = 0.2f,
        color = GColor(0.4f, 0.6f, 0.1f, 0.9f),
        bold = false,
        italic = true,
        font = Font(AssetResourcePath("...path/to/font/in/assets...")),
        spacing = 0.00001f,
        lineSpacing = 0.005f,
        direction = TextDirection.RTL,
        alignment = TextAlignment.NORMAL,
        maxWidth = 0.8f
    ),
    BlenderInstruction(
        center=Vector2f(0.5f, 0.5f)
    )
)
```
![](https://github.com/Colovaria-Graphics/Colovaria-Android/raw/main/image_engine/src/androidTest/assets/test_results/text_drawer/test_2_result.png)

#### Shape Layer
```kotlin
Layer(CircleInstruction(SizeF(0.5f, 0.5f), GColor.RED))
```
![](https://github.com/Colovaria-Graphics/Colovaria-Android/raw/main/image_engine/src/androidTest/assets/test_results/blender/test_1_result.png)

#### Gradient2D layer
```kotlin
Layer(Gradient2DInstruction(mapOf(
    Vector2F(0f, 0f) to GColor(0.1f, 0.1f, 0.0f, 1.0f),
    Vector2F(1f, 1f) to GColor(0.4f, 0.7f, 0.2f, 1.0f),
    Vector2F(1f, 0f) to GColor(0.8f, 0.3f, 0.8f, 1.0f),
    Vector2F(0f, 1f) to GColor(0.5f, 0.8f, 1.0f, 1.0f)
)))
```
![](https://github.com/Colovaria-Graphics/Colovaria-Android/raw/main/image_engine/src/androidTest/assets/test_results/gradient_2d_drawer/test_3_result.png)

#### Image Layer
```kotlin
Layer(ImageInstruction(AssetResourcePath("...path/to/image/in/assets..."), RectF(0.2f, 0.7f, 0.6f, 0.1f)))
```
![](https://github.com/Colovaria-Graphics/Colovaria-Android/raw/main/image_engine/src/androidTest/assets/test_results/image_drawer/test_1_result.png)

When this is the source image:
![](https://github.com/Colovaria-Graphics/Colovaria-Android/raw/main/image_engine/src/androidTest/assets/test_results/image_drawer/test_image.jpg)

#### Layer with Mask
```kotlin
Layer(
    CircleInstruction(SizeF(0.5f, 0.5f), GColor.RED),
    mask=MaskInstruction(
        type=MaskType.ALPHA,
        layer=Layer(RectInstruction(SizeF(1.0f, 0.3f), GColor.WHITE))
    )
)
```
![](https://github.com/Colovaria-Graphics/Colovaria-Android/raw/main/image_engine/src/androidTest/assets/test_results/blender/test_6_result.png)

#### Layer with HSV effect
```kotlin
val layer1 = Layer(ImageInstruction(AssetResourcePath("...path/to/image/in/assets...")))
val layer2 = Layer(HSVInstruction(0.8f, 0.2f, 1.0f))

Frame(listOf(layer1, layer2))
```
![](https://github.com/Colovaria-Graphics/Colovaria-Android/raw/main/image_engine/src/androidTest/assets/test_results/hsv_processor/test_3_result.png)


[image_engine_badge]:https://search.maven.org/artifact/io.github.colovaria-graphics/image_engine  
[image_engine_badge img]:https://img.shields.io/maven-central/v/io.github.colovaria-graphics/image_engine.svg?label=
