package shared.gameObjects.menu.multiplayer.join;

import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.ButtonObject;

public class ButtonConnect extends ButtonObject {

  private transient TextField addressInput;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonConnect(double x, double y, ObjectID id, UUID objectUUID) {
    super(x, y, id, "images/buttons/multiplayer_unpressed.png", objectUUID,
        "images/buttons/multiplayer_pressed.png");
  }

  @Override
  public void initialise(Group root, boolean animate) {
    super.initialise(root, animate);
    addressInput = new TextField();
    addressInput.relocate(getX(), getY() + 300);
    addressInput.setPromptText("Enter IP Address");
  }
}
