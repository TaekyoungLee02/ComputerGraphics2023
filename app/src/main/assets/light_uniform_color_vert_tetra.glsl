#version 300 es

uniform mat4 uMVPMatrix, worldMat;

layout(location = 8) in vec4 vPosition;
layout(location = 9) in vec3 vNormal;

out vec3 fNormal, fPosition;

void main()
{
    gl_Position = uMVPMatrix * vPosition;
    fNormal = normalize(transpose(inverse(mat3(worldMat))) * vNormal);
    //fView = normalize(eyePos - (worldMat * vPosition).xyz);
    fPosition = (worldMat * vPosition).xyz;
}