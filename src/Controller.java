import static org.lwjgl.glfw.GLFW.*;

import java.util.*;
import org.javatuples.*;

public class Controller {
    public long window;
    public HashMap<List<Integer>, Pair<Runnable, Runnable>> inputMap;

    public Controller(long window) {
        this.window = window;
        this.inputMap = new HashMap<>();
    }

    public Controller(long window, HashMap<List<Integer>, Pair<Runnable, Runnable>> inputMap) {
        this.window = window;
        this.inputMap = new HashMap<>();
    }

    public void addInput(List<Integer> key, Runnable onPress, Runnable onRelease) {
        inputMap.put(key, new Pair<>(onPress, onRelease));
    }

    public void monitorInputs() {
        for (Map.Entry<List<Integer>, Pair<Runnable, Runnable>> entry : inputMap.entrySet()) {
            List<Integer> keys = entry.getKey();
            Runnable onPress = entry.getValue().getValue0();
            Runnable onRelease = entry.getValue().getValue1();

            boolean allPressed = true;
            for (int key : keys) {
                if (glfwGetKey(window, key) != GLFW_PRESS && glfwGetMouseButton(window, key) != GLFW_PRESS) {
                    allPressed = false;
                    break;
                }
            }

            if (allPressed) {
                onPress.run();
            } else {
                onRelease.run();
            }
        }
    }
}