package shared.gameObjects.menu;

import client.handlers.audioHandler.AudioHandler;
import client.main.Client;
import client.main.Settings;
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

/**
 * Abstraction of Button Game Objects
 */
public abstract class ButtonObject extends GameObject {

  protected transient Button button;
  protected String text;

  /**
   * Base class used to create a Button in game. This is used on both the client and server side
   * to ensure actions are calculated the same
   *
   * @param x X Position of object
   * @param y Y position of object
   * @param sizeX width of object
   * @param sizeY height of object
   * @param text Text found on the button
   * @param id Object type
   * @param objectUUID Object UUID
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
    new AudioHandler(settings, Client.musicActive).playSFX("CLICK");
  }

  @Override
  public void interpolatePosition(float alpha) {
  }



  public void doOnEnter(MouseEvent e) {
    animation.switchAnimation("clicked");
  }

  public void doOnExit(MouseEvent e) {
    animation.switchDefault();
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    button = new Button(this.text, imageView);
    button.setFont(settings.getFont(30));
    button.setTextFill(Color.WHITE);
    button.setContentDisplay(ContentDisplay.CENTER);
    button.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
    root.getChildren().add(button);
    button.setOnMousePressed(event -> doOnClick(event));
    button.setOnMouseEntered(event -> doOnEnter(event));
    button.setOnMouseExited(event -> doOnExit(event));
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
