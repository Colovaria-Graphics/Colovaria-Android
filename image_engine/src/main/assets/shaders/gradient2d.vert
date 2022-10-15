attribute vec2 position;

varying vec2 pixelPosition;

void main() {
    pixelPosition = (position + vec2(1.0)) / 2.0;
    gl_Position = vec4(position, 0.0, 1.0);
}