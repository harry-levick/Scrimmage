package client.handlers.levelHandler;

import javafx.scene.image.Image;

public class Map {
    private String name;
    private Image mapIcon;
    private String playlist;
    private String path;
    private GameState gameState;

    public Map(String name, String path, GameState gameState) {
        this.name = name;
        this.mapIcon = new Image("");
        this.playlist = "N/A";
        this.path = path;
        this.gameState = gameState;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Image getMapIcon() {

        return mapIcon;
    }

    public String getPlaylist() {
        return playlist;
    }
}
