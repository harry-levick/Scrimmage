package shared.gameObjects.menu.main;

import client.main.Settings;
import java.util.UUID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.SliderObject;

public class MaxPlayerSlider extends SliderObject {

  public MaxPlayerSlider(
      double x,
      double y,
      double sizeX,
      double sizeY,
      ObjectType id,
      UUID objectUUID) {
    super(x, y, sizeX, sizeY, "MAX PLAYERS", id, objectUUID);
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    slider.setMin(2);
    slider.setMax(16);
    slider.setMajorTickUnit(1);
    slider.setMinorTickCount(0);
    slider.setSnapToTicks(true);
    slider
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              public void changed(
                  ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                onValueChange();
              }
            });
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation();
    update();
  }

  public void onValueChange() {
    this.settings.setMaxPlayers((int)slider.getValue());
  }

  @Override
  public void update() {
    slider.setValue(settings.getMaxPlayers());
  }

}
