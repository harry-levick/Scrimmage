package shared.gameObjects.menu;

import client.handlers.audioHandler.AudioHandler;
import client.main.Client;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.AngularData;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

public abstract class ButtonObject extends GameObject {

  protected transient Button button;
  protected String text;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonObject(
      double x, double y, double sizeX, double sizeY, String text, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
    button = new Button(text, imageView);
    this.text = text;
    addComponent(
        new Rigidbody(
            RigidbodyType.STATIC,
            0,
            1,
            0,
            new MaterialProperty(0.1f, 1, 1),
            new AngularData(0, 0, 0, 0),
            this));
    addComponent(new BoxCollider(this, false));
  }


  public void doOnClick(MouseEvent e) {
    animation.switchAnimation("clicked");
    new AudioHandler(settings, Client.musicActive).playSFX("CLICK");
  }

  public void doOnUnClick(MouseEvent e) {
    animation.switchDefault();
  }

  @Override
  public void initialise(Group root) {
    super.initialise(root);
    button = new Button(this.text, imageView);
    button.setFont(Client.settings.getFont(30));
    button.setTextFill(Color.WHITE);
    button.setContentDisplay(ContentDisplay.CENTER);
    button.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
    root.getChildren().add(button);
    button.setOnMousePressed(event -> doOnClick(event));
    button.setOnMouseReleased(event -> doOnUnClick(event));
  }

  @Override
  public void interpolatePosition(float alpha) {

  }

  @Override
  public void render() {
    imageView.setImage(animation.getImage());
    button.setTranslateX(getX());
    button.setTranslateY(getY());
    imageView.setTranslateX(0);
    imageView.setTranslateY(0);
  }

  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/buttons/blank_unpressed.png");
    this.animation.supplyAnimation("clicked", "images/buttons/blank_pressed.png");
  }

  @Override
  public String getState() {
    return null;
  }


  public void removeRender() {
    super.removeRender();
    button.disarm();
    root.getChildren().remove(button);
    button = null;
  }

  public Button getButton() {
    return button;
  }
}
