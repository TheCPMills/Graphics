import java.util.*;
import glm.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.*;

public class Mesh {
    public List<Vertex> vertices;
    public List<Integer> indices;
    public Material material;
    public List<Texture> textures;
    public int drawMode;
    public VAO vao = new VAO();

    public Mesh(List<Vertex> vertices, List<Integer> indices, Material material, List<Texture> textures) {
        this.vertices = vertices;
        this.indices = indices;
        this.material = material;
        this.textures = textures;
        this.drawMode = GL_TRIANGLES;

        setup();
    }

    public Mesh(List<Vertex> vertices, List<Integer> indices, Material material, List<Texture> textures, int drawMode) {
        this.vertices = vertices;
        this.indices = indices;
        this.material = material;
        this.textures = textures;
        this.drawMode = drawMode;

        setup();
    }

    public Mesh(List<Vertex> vertices, ArrayList<vec3> tangents, ArrayList<vec3> bitangents, List<Integer> indices, Material material, List<Texture> textures) {
        this.vertices = vertices;
        this.indices = indices;
        this.material = material;
        this.textures = textures;
        this.drawMode = GL_TRIANGLES;

        setup(tangents, bitangents);
    }

    public Mesh(List<Vertex> vertices, ArrayList<vec3> tangents, ArrayList<vec3> bitangents, List<Integer> indices, Material material, List<Texture> textures, int drawMode) {
        this.vertices = vertices;
        this.indices = indices;
        this.material = material;
        this.textures = textures;
        this.drawMode = drawMode;

        setup(tangents, bitangents);
    }

    private void setup(ArrayList<vec3> tangents, ArrayList<vec3> bitangents) {
        vao.bind();
        VBO vbo = new VBO(vertices); // generates VBO and links it to vertices
        VBO tangentVBO = new VBO(tangents); // generates VBO and links it to tangents
        VBO bitangentVBO = new VBO(bitangents); // generates VBO and links it to bitangents
        EBO ebo = new EBO(indices); // generates EBO and links it to indices

        // link VBO attributes to VAO
        vao.linkAttribute(vbo, 0, 3, GL_FLOAT, 8 * 4, 0);
        vao.linkAttribute(vbo, 1, 3, GL_FLOAT, 8 * 4, 3 * 4);
        vao.linkAttribute(tangentVBO, 2, 3, GL_FLOAT, 3 * 4, 0);
        vao.linkAttribute(bitangentVBO, 3, 3, GL_FLOAT, 3 * 4, 0);
        vao.linkAttribute(vbo, 4, 2, GL_FLOAT, 8 * 4, 6 * 4);

        // unbind all to prevent accidental modification
        vao.unbind();
        vbo.unbind();
        tangentVBO.unbind();
        bitangentVBO.unbind();
        ebo.unbind();
    }

    public void setup() {
        setup(new ArrayList<>(), new ArrayList<>());
    }

    public void draw(Shader shader, Camera camera) throws Exception {
        shader.activate();
        vao.bind();

        int numDiffuse = 0;
        int numSpecular = 0;
        int numNormal = 0;

        for (int i = 0; i < textures.size(); i++) {
            String num = "";
            String type = textures.get(i).type;
            if (type.equals("diffuse")) {
                num = Integer.toString(numDiffuse++);
            }  else if (type.equals("specular")) {
                num = Integer.toString(numSpecular++);
            } else if (type.equals("normal")) {
                num = Integer.toString(numNormal++);
            }
            textures.get(i).textureUnit(shader, type + num, i);
            textures.get(i).bind();
        }

        material.setUniforms(shader);

        glUniform3f(glGetUniformLocation(shader.id, "cameraPosition"), camera.position.x, camera.position.y, camera.position.z);
        camera.update(shader, "cameraMatrix");

        glDrawElements(drawMode, indices.size(), GL_UNSIGNED_INT, 0);
    }
}