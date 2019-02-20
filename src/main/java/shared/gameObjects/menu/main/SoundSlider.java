package shared.gameObjects.menu.main;

import client.main.Client;
import java.util.UUID;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.SliderObject;

public class SoundSlider extends SliderObject {

  SOUND_TYPE soundType = SOUND_TYPE.MUSIC;

  public SoundSlider(double x, double y, double sizeX, double sizeY, SOUND_TYPE soundType,
      ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
    this.soundType = soundType;
    slider.valueProperty()
        .addListener(
            new InvalidationListener() {
              @Override
              public void invalidated(Observable observable) {

                onValueChange();
              }
            });
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation();
    if (this.soundType != null) {
      switch (this.soundType) {
        case MUSIC:
          slider.setValue(Client.settings.getMusicVolume() * 100f);
          System.out.println("SET MUSIC " + Client.settings.getMusicVolume());
          break;
        case SFX:
          slider.setValue(Client.settings.getSoundEffectVolume() * 100f);
          System.out.println("SET SFX " + Client.settings.getSoundEffectVolume());
      }
    }
  }

  public void onValueChange() {
    switch (this.soundType) {
      case MUSIC:
        Client.settings.setMusicVolume(slider.getValue() / 100f);

        break;
      case SFX:
        Client.settings.setSoundEffectVolume(slider.getValue() / 100f);
        break;
    }

  }

  public enum SOUND_TYPE {MUSIC, SFX}
}
