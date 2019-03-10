package shared.gameObjects.menu.multiplayer;

import client.handlers.connectionHandler.ConnectionHandler;
import client.main.Client;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonJoin extends ButtonObject {

  private transient TextField addressInput;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonJoin(double x, double y, double sizeX, double sizeY, ObjectType id,
      UUID objectUUID) {
    super(x, y, sizeX, sizeY, "Join", id, objectUUID);
  }

  public void initialise(Group root) {
    super.initialise(root);
    addressInput = new TextField();
    addressInput.setTranslateX(getX() + 90);
    addressInput.setTranslateY(getY() + 120);
    root.getChildren().add(addressInput);
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation(
        "images/buttons/multiplayer_unpressed.png", "images/buttons/multiplayer_pressed.png");
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.connectionHandler = new ConnectionHandler(addressInput.getText());
    Client.connectionHandler.start();
    button.disarm();
    root.getChildren().remove(addressInput);
    Client.levelHandler.changeMap(
        new Map("Lobby", Path.convert("src/main/resources/menus/lobby.map"), GameState.Lobby),
        false);
  }
}
