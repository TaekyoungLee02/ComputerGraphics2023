#version 300 es

uniform mat4 uMVPMatrix, worldMat;

layout(location= 1) in vec4 vPosition;
layout(location= 2) in vec4 vColor;

out vec4 fColor;

void main()
{
    gl_Position = uMVPMatrix * vPosition;
    fColor = vColor;
}