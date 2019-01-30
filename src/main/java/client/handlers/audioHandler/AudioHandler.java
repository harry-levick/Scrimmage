package client.handlers.audioHandler;

import client.main.Settings;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioHandler {

  private MediaPlayer musicPlayer;
  private MediaPlayer effectPlayer;
  private Media musicMedia;
  private Media effectMedia;
  private Settings settings;

  private MusicAssets musicAssets = new MusicAssets();
  private EffectsAssets effectsAssets = new EffectsAssets();

  public AudioHandler(Settings settings) {
    this.settings = settings;
  }

  /**
   * Starts game music and continuously plays it
   *
   * @param trackName Music resource to play
   */
  public void playMusic(String trackName) {
    String path = musicAssets.getTrackPath(trackName);
    if (!(path == null)) {
      musicMedia = new Media(new File(path).toURI().toString());
      musicPlayer = new MediaPlayer(musicMedia);
      updateMusicVolume();
      musicPlayer.play();
    } else {
      // todo log error
    }
  }

  /** Stop any game music from playing */
  public void stopMusic() {
    if (musicPlayer != null) {
      musicPlayer.stop();
    }
  }

  /**
   * Plays in game sound effect
   *
   * @param trackName sound effect resource
   */
  public void playSFX(String trackName) {
    String path = effectsAssets.getTrackPath(trackName);
    if (!(path == null)) {
      effectMedia = new Media(new File(path).toURI().toString());
      effectPlayer = new MediaPlayer(effectMedia);
      updateEffectVolume();
      effectPlayer.play();
    } else {
      // todo log error
    }
  }

  public void stopSFX() {
    if (effectPlayer != null) {
      effectPlayer.stop();
    }
  }

  public void updateMusicVolume() {
    if (musicPlayer != null) {
      musicPlayer.setVolume(settings.getMusicVolume());
    }
  }

  public void updateEffectVolume() {
    if (effectPlayer != null) {
      effectPlayer.setVolume(settings.getSoundEffectVolume());
    }
  }
}