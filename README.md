
# ![ezgif-3-0d98430e38](https://user-images.githubusercontent.com/14913725/191113865-abe9b600-8d54-426a-b110-98313d5f8098.gif)
  
🎨 2D layers-based image rendering engine for Android based on GLES, that works like Magic! 🪄  

[![][build img]][build]  

## Libraries:
### Main library:
 - [Image Engine](https://github.com/Colovaria-Graphics/Colovaria-Android/blob/main/image_engine/README.md) - [![][image_engine_badge img]][image_engine_badge]
 
 	Library that converts visual instructions to pixels on the screen.

### Utils libraries:
 - [Graphics](https://github.com/Colovaria-Graphics/Colovaria-Android/blob/main/graphics/README.md) -  [![][graphics_badge img]][graphics_badge]
 	
	Basic Kotlin wrap for OpenGLES state machine.
	 
 - [Geometry](https://github.com/Colovaria-Graphics/Colovaria-Android/blob/main/geometry/README.md) - [![][geometry_badge img]][geometry_badge]  
	
	Pure kotlin library for geometric shapes representation and calculations.
 
## Usage:

### Instructions:
See each library spesific README for specific instructions:
 - [Image Engine README](https://github.com/Colovaria-Graphics/Colovaria-Android/blob/main/image_engine/README.md)
 - [Graphics README](https://github.com/Colovaria-Graphics/Colovaria-Android/blob/main/graphics/README.md)
 - [Geometry README](https://github.com/Colovaria-Graphics/Colovaria-Android/blob/main/geometry/README.md)
 
### Dependencies:
```kotlin
// For image engine:
implementation "io.github.colovaria-graphics:image_engine:+"

// For graphics:
implementation "io.github.colovaria-graphics:graphics:+"

// For geometry:
implementation "io.github.colovaria-graphics:geometry:+"
```
 
[build]:https://github.com/Colovaria-Graphics/Colovaria-Android/actions/workflows/android_build.yml  
[build img]:https://github.com/Colovaria-Graphics/Colovaria-Android/actions/workflows/android_build.yml/badge.svg?branch=main  
  
[image_engine_badge]:https://search.maven.org/artifact/io.github.colovaria-graphics/image_engine  
[image_engine_badge img]:https://img.shields.io/maven-central/v/io.github.colovaria-graphics/image_engine.svg?label=ImageEngine  
  
[graphics_badge]:https://search.maven.org/artifact/io.github.colovaria-graphics/graphics  
[graphics_badge img]:https://img.shields.io/maven-central/v/io.github.colovaria-graphics/graphics.svg?label=Graphics  
  
[geometry_badge]:https://search.maven.org/artifact/io.github.colovaria-graphics/geometry  
[geometry_badge img]:https://img.shields.io/maven-central/v/io.github.colovaria-graphics/geometry.svg?label=Geometry
