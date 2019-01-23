package client.handlers.inputHandler;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyboardInput implements EventHandler<KeyEvent> {

  // TODO Add more keyboard controls
  public static boolean leftKey, rightKey, jumpKey;

  public KeyboardInput() {
    leftKey = false;
    rightKey = false;
    jumpKey = false;
  }

  @Override
  public void handle(KeyEvent event) {
    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
      switch (event.getCode()) {
        case A:
          leftKey = true;
          break;
        case D:
          rightKey = true;
          break;
        case SPACE:
          jumpKey = true;
          break;
        default:
      }
    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      switch (event.getCode()) {
        case A:
          leftKey = false;
          break;
        case D:
          rightKey = false;
          break;
        case SPACE:
          jumpKey = false;
          break;
        default:
      }
    }
  }
}
