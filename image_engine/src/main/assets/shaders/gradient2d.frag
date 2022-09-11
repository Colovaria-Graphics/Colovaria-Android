#version 300 es

#define MAX_ACTIVE_DOTS (6)

precision mediump float;

uniform vec3 colors[MAX_ACTIVE_DOTS];
uniform vec2 positions[MAX_ACTIVE_DOTS];
uniform int activeDots;

in vec2 pixelPosition;

out vec4 fragColor;

void main() {
    vec3 color = vec3(0.0);
    float totalWeight = 0.0;

    for (int i = 0; i < activeDots; i++) {
        float weight = pow(positions[i].x - pixelPosition.x, 2.0) + pow(positions[i].y - pixelPosition.y, 2.0);

        if (abs(weight) <= 0.0001) {
            color = colors[i];
            totalWeight = 1.0;
            break;
        }
        weight = 1.0 / weight;

        color += colors[i] * weight;
        totalWeight += weight;
    }

    color /= totalWeight;

    fragColor = vec4(color, 1.0);
}