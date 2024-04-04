import glm.*;
import java.util.*;

public class Vertex {
    private vec3 position;
    private vec3 normal;
    private vec2 uv;

    public Vertex(vec3 position, vec3 normal, vec2 uv) {
        this.position = position;
        this.normal = normal;
        this.uv = uv;
    }

    public vec3 position() {
        return position;
    }

    public boolean position(vec3 position) {
        this.position = position;
        return true;
    }

    public vec3 normal() {
        return normal;
    }

    public boolean normal(vec3 normal) {
        this.normal = normal;
        return true;
    }

    public vec2 uv() {
        return uv;
    }

    public boolean uv(vec2 uv) {
        this.uv = uv;
        return true;
    }

    public static float[] vertexArray(List<Vertex> vertices) {
        float[] vertexArray = new float[vertices.size() * 8];
        for (int i = 0; i < vertices.size(); i++) {
            vertexArray[i * 8 + 0] = vertices.get(i).position.x;
            vertexArray[i * 8 + 1] = vertices.get(i).position.y;
            vertexArray[i * 8 + 2] = vertices.get(i).position.z;
            vertexArray[i * 8 + 3] = vertices.get(i).normal.x;
            vertexArray[i * 8 + 4] = vertices.get(i).normal.y;
            vertexArray[i * 8 + 5] = vertices.get(i).normal.z;
            vertexArray[i * 8 + 6] = vertices.get(i).uv.x;
            vertexArray[i * 8 + 7] = vertices.get(i).uv.y;
        }
        return vertexArray;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vertex)) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return position.equals(vertex.position) && normal.equals(vertex.normal) && uv.equals(vertex.uv);
    }
}