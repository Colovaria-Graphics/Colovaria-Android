#define COMPUTE_COLOR_AND_RETURN gl_FragColor = vec4(color / totalWeight, 1.0); return

precision mediump float;

uniform vec3 color0;
uniform vec3 color1;
uniform vec3 color2;
uniform vec3 color3;
uniform vec3 color4;
uniform vec3 color5;

uniform vec2 position0;
uniform vec2 position1;
uniform vec2 position2;
uniform vec2 position3;
uniform vec2 position4;
uniform vec2 position5;

uniform int activePoints;

varying vec2 pixelPosition;

float computePointWeight(vec2 position, vec2 colorPosition) {
    vec2 powDistance = (position - colorPosition) * (position - colorPosition);
    return 1.0 / (powDistance.x + powDistance.y);
}

void main() {
    vec3 color = vec3(0.0);
    float totalWeight = 0.0;

    float weight0 = computePointWeight(pixelPosition, position0);
    color += color0 * weight0;
    totalWeight += weight0;
    if (activePoints == 1) { COMPUTE_COLOR_AND_RETURN; }

    float weight1 = computePointWeight(pixelPosition, position1);
    color += color1 * weight1;
    totalWeight += weight1;
    if (activePoints == 2) { COMPUTE_COLOR_AND_RETURN; }

    float weight2 = computePointWeight(pixelPosition, position2);
    color += color2 * weight2;
    totalWeight += weight2;
    if (activePoints == 3) { COMPUTE_COLOR_AND_RETURN; }

    float weight3 = computePointWeight(pixelPosition, position3);
    color += color3 * weight3;
    totalWeight += weight3;
    if (activePoints == 4) { COMPUTE_COLOR_AND_RETURN; }

    float weight4 = computePointWeight(pixelPosition, position4);
    color += color4 * weight4;
    totalWeight += weight4;
    if (activePoints == 5) { COMPUTE_COLOR_AND_RETURN; }

    float weight5 = computePointWeight(pixelPosition, position5);
    color += color5 * weight5;
    totalWeight += weight5;

    COMPUTE_COLOR_AND_RETURN;
}