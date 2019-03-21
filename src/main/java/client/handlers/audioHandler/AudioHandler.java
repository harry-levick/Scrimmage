package client.handlers.audioHandler;

import client.handlers.audioHandler.MusicAssets.PLAYLIST;
import client.main.Settings;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Handles the audio (SFX and Music) for the game
 */
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

  private MusicAssets musicAssets;
  private EffectsAssets effectsAssets;

  public AudioHandler(Settings settings, boolean active) {
    this.settings = settings;
    this.active = active;
    musicAssets = new MusicAssets(settings);
    effectsAssets = new EffectsAssets(settings);
  }

  /**
   * Starts game music and continuously plays it. Stops previous track if a new one is played.
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

  /**
   * Starts playing a specified music playlist (specified in MusicAssets). Loops at the end of the
   * playlist.
   *
   * @param playlistSet The playlist of type PLAYLIST, from MusicAssets, to play
   */
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
   * Plays in game sound effect. Sound effects can overlap. Can be stopped with stopSFX()
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

  /**
   * Increment the track index, for use in playlist
   */
  private void incrementTrack() {
    if (active) {
      trackPos++;
      if (trackPos == playlist.size()) {
        trackPos = 0;
      }
    }
  }

  /**
   * Stops the current sound effect from playing
   */
  public void stopSFX() {
    if (active && effectPlayer != null) {
      effectPlayer.stop();
    }
  }

  /**
   * Set the volume of the music to play at the volume defined in settings
   */
  public void updateMusicVolume() {
    if (active && musicPlayer != null) {
      musicPlayer.setVolume(settings.getMusicVolume());
    }
  }

  /**
   * Set the volume of the sound effect to play at the volume defined in settings
   */
  public void updateEffectVolume() {
    if (active && effectPlayer != null) {
      effectPlayer.setVolume(settings.getSoundEffectVolume());
    }
  }

  /**
   * If this AudioHandler is active
   * @return The state of this handler. Active is true if it plays sounds
   */
  public boolean getActive() {
    return this.active;
  }

  /**
   * Sets the state of the AudioHandler. True if it plays sound
   * @param active boolean state of the handler
   */
  public void setActive(boolean active) {
    this.active = active;
  }
}
