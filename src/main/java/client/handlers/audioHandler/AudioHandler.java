package client.handlers.audioHandler;

import client.main.Settings;
import java.io.File;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioHandler {

  private MediaPlayer musicPlayer;
  private Media media;
  private Settings settings;

  private MusicAssets musicAssets = new MusicAssets(); // todo make static

  public AudioHandler(Settings settings) {
    this.settings = settings;
  }

  // TODO Add update volume level

  /**
   * Starts game music and continuously plays it
   *
   * @param trackName Music resource to play
   */
  public void playMusic(String trackName) {
    //    musicPlayer = new MediaPlayer(media);
    //    musicPlayer.setVolume(settings.getMusicVolume());
    //    musicPlayer.setOnEndOfMedia(() -> playMusic(media));
    //    musicPlayer.play();
    String path = musicAssets.getFUNK_GAME_LOOP();
    media = new Media(new File(path).toURI().toString());
    musicPlayer = new MediaPlayer(media);
    updateVolume();
    musicPlayer.play();
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
   * @param media sound effect resource
   */
  public void playSFX(AudioClip media) {
    media.setVolume(settings.getSoundEffectVolume());
    media.play();
  }

  public void setMusicVolume(double volume) {
    musicPlayer.setVolume(volume);
  }


  public void updateVolume() {
    musicPlayer.setVolume(settings.getMusicVolume());
  }
}