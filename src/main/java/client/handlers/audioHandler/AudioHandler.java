package client.handlers.audioHandler;

import client.handlers.audioHandler.MusicAssets.PLAYLIST;
import client.main.Settings;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioHandler {

  private MediaPlayer musicPlayer;
  private MediaPlayer effectPlayer;
  private Media musicMedia;
  private Media effectMedia;
  private Settings settings;

  private int trackPos = 0;
  private ArrayList<String> playlist = new ArrayList<>();
  private boolean playingPlaylist = false;
  private PLAYLIST currentPlaylist;
  private boolean active;

  private MusicAssets musicAssets = new MusicAssets();
  private EffectsAssets effectsAssets = new EffectsAssets();

  public AudioHandler(Settings settings, boolean active) {
    this.settings = settings;
    this.active = active;
  }

  /**
   * Starts game music and continuously plays it
   *
   * @param trackName Music resource to play
   */
  public void playMusic(String trackName) {
    if (active) {
      String path = musicAssets.getTrackPath(trackName);
      stopMusic();
      if (!(path == null)) {
        musicMedia = new Media(new File(path).toURI().toString());
        musicPlayer = new MediaPlayer(musicMedia);
        updateMusicVolume();
        musicPlayer.play();
      } else {
        // todo log error
      }
    }
  }

  public void playMusicPlaylist(PLAYLIST playlistSet) {
    if (active) {
      if ((currentPlaylist != null) && (playlistSet != currentPlaylist)) {
        //playlist change
        trackPos = 0;
      }
      currentPlaylist = playlistSet;
      playingPlaylist = true;
      playlist = musicAssets.getPlaylist(playlistSet);
      playMusic(playlist.get(trackPos));
      musicPlayer.setOnEndOfMedia(new Runnable() {
        @Override
        public void run() {
          incrementTrack();
          playMusicPlaylist(playlistSet);
        }
      });
    }
  }

  /**
   * Stop any game music from playing
   */
  public void stopMusic() {
    if (active) {
      if (musicPlayer != null) {
        if (playingPlaylist) {
          incrementTrack();
        }
        musicPlayer.stop();
      }
    }
  }

  /**
   * Plays in game sound effect
   *
   * @param trackName sound effect resource
   */
  public void playSFX(String trackName) {
    if (active) {
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
  }

  private void incrementTrack() {
    if (active) {
      trackPos++;
      if (trackPos == playlist.size()) {
        trackPos = 0;
      }
    }
  }

  public void stopSFX() {
    if (active && effectPlayer != null) {
      effectPlayer.stop();
    }
  }

  public void updateMusicVolume() {
    if (active && musicPlayer != null) {
      musicPlayer.setVolume(settings.getMusicVolume());
    }
  }

  public void updateEffectVolume() {
    if (active && effectPlayer != null) {
      effectPlayer.setVolume(settings.getSoundEffectVolume());
    }
  }

  public boolean getActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
