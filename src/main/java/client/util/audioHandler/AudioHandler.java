package client.util.audioHandler;

import client.main.Settings;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioHandler {

    private MediaPlayer musicPlayer;
    private Settings settings;

    public AudioHandler(Settings settings) {
        this.settings = settings;
    }

    //TODO Add update volume level

    /**
     * Starts game music and continuously  plays it
     * @param media Music resource to play
     */
    public void playMusic(Media media) {
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setVolume(settings.getMusicVolume());
        musicPlayer.setOnEndOfMedia(() -> playMusic(media));
        musicPlayer.play();
    }

    /**
     * Stop any game music from playing
     */
    public void stopMusic() {
        if (musicPlayer != null) {musicPlayer.stop();}
    }

    /**
     * Plays in game sound effect
     * @param media sound effect resource
     */
    public void playSFX(AudioClip media) {
        media.setVolume(settings.getSoundEffectVolume());
        media.play();
    }
}
