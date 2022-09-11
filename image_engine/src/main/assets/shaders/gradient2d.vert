#version 300 es

in vec2 position;
in vec2 texturePosition;

out vec2 pixelPosition;

void main() {
    pixelPosition = texturePosition;
    gl_Position = vec4(position, 0.0, 1.0);
}