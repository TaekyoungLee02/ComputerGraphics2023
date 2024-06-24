#version 300 es

uniform mat4 uMVPMatrix, worldMat;
uniform vec3 eyePos;

layout(location = 10) in vec4 vPosition;
layout(location = 11) in vec2 vTexCoord;
layout(location = 12) in vec3 vNormal;

out vec2 fTexCoord;
out vec3 fNormal, fView;

void main()
{
    gl_Position = uMVPMatrix * vPosition;
    fTexCoord = vTexCoord;
    fNormal = normalize(transpose(inverse(mat3(worldMat))) * vNormal);
    //fNormal = vNormal;
    fView = normalize(eyePos - (worldMat * vPosition).xyz);
    //fView = normalize(vec3(2.0f, 2.0f, 2.0f) - vPosition.xyz);
}