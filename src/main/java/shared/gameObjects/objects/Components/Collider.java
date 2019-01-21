package shared.gameObjects.objects.Components;

import java.io.Serializable;

/**
 * @author fxa579
 * Primary component responsible for collider info, such as collision state, size, shape
 */
public class Collider extends Component implements Serializable {
    boolean collisionEnter, collisionExit, collisionStay, triggerEnter, triggerExit, triggerStay;


}
