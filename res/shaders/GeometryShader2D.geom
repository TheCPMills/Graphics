#version 330 core
layout (lines) in;
layout (line_strip, max_vertices = 2) out;

in DATA {
    vec3 normal;
    vec2 textureCoordinates;
    mat4 projection;
} dataIn[];

out vec3 fragmentPosition;
out vec3 fragmentNormal;
out vec2 textureCoordinates;

void main() {
	gl_Position = dataIn[0].projection * gl_in[0].gl_Position;
    fragmentNormal = dataIn[0].normal;
    textureCoordinates = dataIn[0].textureCoordinates;
    fragmentPosition = gl_in[0].gl_Position.xyz;
    EmitVertex();

    gl_Position = dataIn[1].projection * gl_in[1].gl_Position;
    fragmentNormal = dataIn[1].normal;
    textureCoordinates = dataIn[1].textureCoordinates;
    fragmentPosition = gl_in[1].gl_Position.xyz;
    EmitVertex();

    EndPrimitive();
}