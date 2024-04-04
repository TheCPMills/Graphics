import glm.*;
import static org.lwjgl.opengl.GL20C.*;

public class Camera {
    public vec3 position, orientation, target, worldUp;
    public mat4 cameraMatrix = mat4.identity();

    public int width, height;
    public mat4 projectionMatrix;
    protected float speed = 0.05f;
    protected float sensitivity = 100.0f;

    public Camera(int width, int height, mat4 projectionMatrix, vec3 position) {
        this.width = width;
        this.height = height;
        this.projectionMatrix = projectionMatrix;
        this.position = position;
        setOrientation(new vec3(0f, 0f, 1f));
        this.worldUp = new vec3(0f, 1f, 0f);
    }

    public Camera(int width, int height, mat4 projectionMatrix, vec3 position, vec3 target) {
        this.width = width;
        this.height = height;
        this.projectionMatrix = projectionMatrix;
        this.position = position;
        setTarget(target);
        this.worldUp = new vec3(0f, 1f, 0f);
    }

    public void update(Shader shader, String uniform) {
        mat4 viewMatrix = glm.lookAt(position, target, worldUp);

        cameraMatrix = projectionMatrix.mult(viewMatrix);
        glUniformMatrix4fv(glGetUniformLocation(shader.id, uniform), false, cameraMatrix.toBuffer());
    }

    public void setPosition(vec3 eye) {
        this.position = eye;
        this.target = position.add(orientation.negate());
    }

    public void setTarget(vec3 target) {
        this.target = target;
        this.orientation = position.sub(target).normalize();
    }

    public void setOrientation(vec3 orientation) {
        this.orientation = orientation.normalize();
        this.target = position.add(orientation.negate());
    }

    public void setWorldUp(vec3 worldUp) {
        this.worldUp = worldUp;
    }
}