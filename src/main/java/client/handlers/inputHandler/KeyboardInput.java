package client.handlers.inputHandler;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyboardInput implements EventHandler<KeyEvent> {

  public static boolean keyPressed;
  public static boolean leftKey, rightKey, jumpKey;

  public KeyboardInput() {
    leftKey = false;
    rightKey = false;
    jumpKey = false;
  }

  public static String getInput() {
    return "INPUT:" + leftKey + "," + rightKey + "," + jumpKey;
  }

  @Override
  public void handle(KeyEvent event) {
    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
      switch (event.getCode()) {
        case A:
          leftKey = true;
          keyPressed = true;
          break;
        case D:
          rightKey = true;
          keyPressed = true;
          break;
        case SPACE:
          jumpKey = true;
          keyPressed = true;
          break;
        default:
      }
    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      switch (event.getCode()) {
        case A:
          leftKey = false;
          keyPressed = false;
          break;
        case D:
          rightKey = false;
          keyPressed = false;
          break;
        case SPACE:
          jumpKey = false;
          keyPressed = false;
          break;
        default:
      }
    }
  }
}
