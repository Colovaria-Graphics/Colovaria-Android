precision mediump float;

uniform int blendMode;
uniform float opacity;
uniform float maskOpacity;

uniform int maskEnabled;
uniform int maskType;

uniform sampler2D backTexture;
uniform sampler2D frontTexture;
uniform sampler2D maskTexture;

varying vec2 backTexturePosition;
varying vec2 frontTexturePosition;
varying vec2 maskTexturePosition;

vec4 normalBlend(vec4 colorA, vec4 colorB) {
    return mix(colorA, colorB, colorB.a);
}

vec4 darkenBlend(vec4 colorA, vec4 colorB) {
    return min(colorA, colorB);
}

vec4 lightenBlend(vec4 colorA, vec4 colorB) {
    return max(colorA, colorB);
}

vec4 multiplyBlend(vec4 colorA, vec4 colorB) {
    return colorA * colorB;
}

vec4 differenceBlend(vec4 colorA, vec4 colorB) {
    return abs(colorA - colorB);
}

vec4 blend(vec4 colorA, vec4 colorB, int blendMode) {
    if (blendMode == 1) {
        return darkenBlend(colorA, colorB);
    } else if (blendMode == 2) {
        return lightenBlend(colorA, colorB);
    } else if (blendMode == 3) {
        return multiplyBlend(colorA, colorB);
    } else if (blendMode == 4) {
        return differenceBlend(colorA, colorB);
    } else {
        return normalBlend(colorA, colorB);
    }
}

float mask(sampler2D maskTexture, vec2 maskTexturePosition, float maskOpacity, int maskType) {
    if (maskType == 0) {
        // Alpah mask.
        return texture(maskTexture, maskTexturePosition).a * maskOpacity;
    } else {
        return 1.0;
    }
}

void main() {
    vec4 backColor = texture(backTexture, backTexturePosition);
    vec4 frontColor = texture(frontTexture, frontTexturePosition);
    vec4 layerColor = blend(backColor, frontColor, blendMode);

    float maskValue = 1.0;
    if (maskEnabled != 0) {
        maskValue = mask(maskTexture, maskTexturePosition, maskOpacity, maskType);
    }

    gl_FragColor = mix(backColor, layerColor, opacity * frontColor.a * maskValue);
}