package shared.gameObjects.menu.multiplayer;

import client.handlers.networkHandlers.ConnectionHandler;
import client.main.Client;
import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

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
    //TODO note: this does not currently get removed by the gamObject clear when changing maps
  }


  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.connectionHandler = new ConnectionHandler(addressInput.getText());
    Client.connectionHandler.start();
    button.disarm();
    root.getChildren().remove(addressInput);
    Client.levelHandler.changeMap(Settings.getMultiplayerLobby(), false);
  }
}
