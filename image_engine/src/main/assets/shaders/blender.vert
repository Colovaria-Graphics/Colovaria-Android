#version 300 es

in vec2 position;
in vec2 texturePosition;

uniform mat4 model;
uniform mat4 frontModel;
uniform mat4 maskModel;

out vec2 backTexturePosition;
out vec2 frontTexturePosition;
out vec2 maskTexturePosition;

void main() {
    backTexturePosition = texturePosition;
    frontTexturePosition = (frontModel * vec4(texturePosition, 0.0, 1.0)).xy;
    maskTexturePosition = (maskModel * vec4(texturePosition, 0.0, 1.0)).xy;

    gl_Position = model * vec4(position, 0.0, 1.0);
}
