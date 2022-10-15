attribute vec2 position;
attribute vec2 texturePosition;

varying vec2 backTexturePosition;

void main() {
    backTexturePosition = texturePosition;
    gl_Position = vec4(position, 0.0, 1.0);
}