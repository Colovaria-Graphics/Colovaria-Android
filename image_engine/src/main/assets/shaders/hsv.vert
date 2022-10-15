attribute vec2 position;

varying vec2 backTexturePosition;

void main() {
    backTexturePosition = (position + vec2(1.0)) / 2.0;
    gl_Position = vec4(position, 0.0, 1.0);
}