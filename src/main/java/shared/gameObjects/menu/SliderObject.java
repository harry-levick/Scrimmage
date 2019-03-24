package shared.gameObjects.menu;

import client.main.Client;
import client.main.Settings;
import com.jfoenix.controls.JFXSlider;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

/**
 * Abstraction of slider object
 */
public abstract class SliderObject extends GameObject {

  private static final float yOffset = 20;
  protected transient JFXSlider slider;
  protected transient Text text;
  protected String label;

  public SliderObject(
      double x, double y, double sizeX, double sizeY, String label, ObjectType id,
      UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
    slider = new JFXSlider();
    this.label = label;
  }


  @Override
  public void initialise(Group root, Settings settings) {
    slider = new JFXSlider();
    super.initialise(root, settings);
    slider.setLayoutX(getX());
    slider.setLayoutY(getY() + yOffset);
    slider.setMinSize(transform.getSize().getX(), transform.getSize().getY() + 30);
    slider.setMaxSize(transform.getSize().getX(), transform.getSize().getY() + 30);
    root.getChildren().add(slider);
    text = new Text(label);
    text.setLayoutX(getX());
    text.setLayoutY(getY());
    text.setFont(Client.settings.getFont(32));
    root.getChildren().add(text);
    text.setFill(Color.WHITE);
  }

  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/buttons/slider_thumb.png");
  }

  @Override
  public void render() {

  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().remove(slider);
    root.getChildren().remove(text);
    slider = null;
  }

  public Slider getSlider() {
    return slider;
  }
}
