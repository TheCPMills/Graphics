import glm.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import java.nio.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.util.*;

public class App {
    // =================
    // ==== GLOBALS ====
    // =================
    private static final int width = 1920;
    private static final int height = 1080;
    private static int useTexture = 1;
    private static boolean qKeyDown = false;

    // ==============================
    // ==== MODEL INITIALIZATION ====
    // ==============================
    private static Model objectModel;
    private static mat4 modelMatrix;
        
    // =================================
    // ==== LIGHTING INITIALIZATION ====
    // =================================
    private static ArrayList<Light> lights = new ArrayList<>();

    // ===============================
    // ==== CAMERA INITIALIZATION ====
    // ===============================
    private static final vec3 cameraPosition = new vec3(2.0f, 2.0f, -2.0f);
    private static final vec3 cameraOrientation = new vec3(-1.0f, -1.0f, 1.0f);
    private static Camera camera;

    // =================
    // ==== PROGRAM ====
    // =================
    public static void main(String[] args) throws Exception {
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_SAMPLES, 4); // 4x antialiasing
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Create the window
        long window = glfwCreateWindow(width, height, "Scramble: ", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        // specify the viewport in the window
        glViewport(0, 0, width, height);

        // enable key press capture
        glfwSetInputMode(window, GLFW_STICKY_KEYS, GLFW_TRUE);

        // enable the depth buffer and backface culling
	    glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CCW);

        // set background color
        glClearColor(0.9f, 0.9f, 0.9f, 1.0f);

        // Initialize models, lighting, and camera
        initializeMLC();

        // set up shadows
        FBO shadowMap = new FBO(4096, 4096);
        shadowMap.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Shader shadowShader = new Shader("ShadowVertexShader.vert", "ShadowFragmentShader.frag");
        shadowShader.activate();
        glUniformMatrix4fv(glGetUniformLocation(shadowShader.id, "modelMatrix"), false, modelMatrix.toBuffer());
        objectModel.draw(shadowShader, camera);
        shadowMap.unbind();

        // initialize and activate shader program
        Shader shaderProgram = new Shader("VertexShader.vert", "GeometryShader3D.geom", "FragmentShader.frag");
	    shaderProgram.activate();

        // initialize uniforms
	    glUniformMatrix4fv(glGetUniformLocation(shaderProgram.id, "modelMatrix"), false, modelMatrix.toBuffer());
        glUniform1i(glGetUniformLocation(shaderProgram.id, "useTexture"), useTexture);

        // setup controller
        Controller controller = new Controller(window);
        setupController(controller, shaderProgram);

        // render scene
        render(window, shaderProgram, controller);

        // delete all created objects
        shaderProgram.delete();

        // delete window
        glfwDestroyWindow(window);

        // terminate GLFW
        glfwTerminate();
    }

    private static void render(long window, Shader shaderProgram, Controller controller) throws Exception {
        while (!glfwWindowShouldClose(window)) {
            // clear the screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // check for user input
            controller.monitorInputs();

            // update lights
            Light.update(shaderProgram, lights);

            // update camera
            camera.update(shaderProgram, "cameraMatrix");

            // draw mesh
            objectModel.draw(shaderProgram, camera);

            // update title
            glfwSetWindowTitle(window, "");
            
            // swap back buffer and front buffer
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private static void initializeMLC() throws Exception {
        // model initialization
        objectModel = new Model("referenceCube.obj");
        modelMatrix = mat4.identity();

        // lighting initialization
        lights.add(new PointLight(new vec3(-0.375f, 0.75f, 0.375f), new vec3(2.0f, 3.5f, 2.5f), 0.5f, new vec4(2.5f, 2.45f, 2.5f, 2.5f).scale(0.4f)));
        lights.add(new PointLight(new vec3(0.375f, 0.75f, -0.375f), new vec3(2.0f, 3.5f, 2.5f), 0.5f, new vec4(2.5f, 2.45f, 2.5f, 2.5f).scale(0.4f)));

        // camera initialization
        mat4 projectionMatrix = glm.perspective(70.0f, (float) width / height, 0.1f, 100.0f);
        camera = new Camera(width, height, projectionMatrix, cameraPosition);
        camera.setOrientation(cameraOrientation);
    }

    private static void setupController(Controller controller, Shader shader) {
        controller.addInput(Arrays.asList(GLFW_KEY_W), () -> {
            camera.setPosition(camera.position.add(camera.orientation.scale(camera.speed)));
        }, () -> {});

        controller.addInput(Arrays.asList(GLFW_KEY_A), () -> {
            camera.setPosition(camera.position.add(camera.orientation.cross(camera.worldUp).normalize().scale(camera.speed)));
        }, () -> {});

        controller.addInput(Arrays.asList(GLFW_KEY_S), () -> {
            camera.setPosition(camera.position.add(camera.orientation.scale(-camera.speed)));
        }, () -> {});

        controller.addInput(Arrays.asList(GLFW_KEY_D), () -> {
            camera.setPosition(camera.position.add(camera.orientation.cross(camera.worldUp).normalize().negate().scale(camera.speed)));
        }, () -> {});

        controller.addInput(Arrays.asList(GLFW_KEY_SPACE), () -> {
            camera.setPosition(camera.position.add(camera.worldUp.scale(camera.speed)));
        }, () -> {});

        controller.addInput(Arrays.asList(GLFW_KEY_LEFT_SHIFT), () -> {
            camera.setPosition(camera.position.add(camera.worldUp.scale(-camera.speed)));
        }, () -> {});
        
        controller.addInput(Arrays.asList(GLFW_MOUSE_BUTTON_LEFT), () -> {
            glfwSetInputMode(controller.window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

            DoubleBuffer mouseXBuffer = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer mouseYBuffer = BufferUtils.createDoubleBuffer(1);

            glfwGetCursorPos(controller.window, mouseXBuffer, mouseYBuffer);

            float mouseX = (float) mouseXBuffer.get(0);
            float mouseY = (float) mouseYBuffer.get(0);

            float rotX = camera.sensitivity * (mouseY - (height / 2)) / height;
            float rotY = camera.sensitivity * (mouseX - (width / 2)) / width;

            vec3 newOrientation = glm.rotate(camera.orientation.negate(),
            camera.orientation.negate().cross(camera.worldUp).normalize(),
            glm.radians(rotX));

            if (Math.abs(newOrientation.angle(camera.worldUp) - glm.radians(90.0f)) <=
            glm.radians(85.0f)) {
            camera.setOrientation(newOrientation);
            }

            camera.setOrientation(glm.rotate(camera.orientation.negate(), camera.worldUp,
            glm.radians(rotY)));

            glfwSetCursorPos(controller.window, (width / 2), (height / 2));
        }, () -> {
            glfwSetInputMode(controller.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        });

        controller.addInput(Arrays.asList(GLFW_KEY_Q), () -> {
            if (!qKeyDown) {
                qKeyDown = true;
                useTexture = (useTexture + 1) % 2;
                glUniform1i(glGetUniformLocation(shader.id, "useTexture"), useTexture);
            }
        }, () -> {
            qKeyDown = false;
        });

        controller.addInput(Arrays.asList(GLFW_KEY_BACKSPACE), () -> {
            camera.setPosition(cameraPosition);
            camera.setOrientation(cameraOrientation);
        }, () -> {});
    }
}