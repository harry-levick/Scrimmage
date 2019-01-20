package client.util.inputHandler;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyboardInput implements EventHandler<KeyEvent> {

    //TODO Add more keyboard controls
    public boolean upKey;

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (event.getCode()) {
                case W:
                    upKey = true;
                    break;
                default:
            }
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            switch (event.getCode()) {
                case W:
                    upKey = false;
                    break;
                default:
            }
        }
    }
}
