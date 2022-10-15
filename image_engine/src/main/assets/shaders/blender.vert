attribute vec2 position;
attribute vec2 texturePosition;

uniform mat4 model;
uniform mat4 frontModel;
uniform mat4 maskModel;

varying vec2 backTexturePosition;
varying vec2 frontTexturePosition;
varying vec2 maskTexturePosition;

void main() {
    backTexturePosition = texturePosition;
    frontTexturePosition = (frontModel * vec4(texturePosition, 0.0, 1.0)).xy;
    maskTexturePosition = (maskModel * vec4(texturePosition, 0.0, 1.0)).xy;

    gl_Position = model * vec4(position, 0.0, 1.0);
}
