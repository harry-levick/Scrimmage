package client.handlers.inputHandler;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyboardInput extends InputHandler implements EventHandler<KeyEvent> {

  private InputHandler inputHandler;

  public KeyboardInput(InputHandler inputHandler) {
    this.inputHandler = inputHandler;
  }

  @Override
  public void handle(KeyEvent event) {
    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
      switch (event.getCode()) {
        case A:
          inputHandler.leftKey = true;
          inputHandler.keyPressed = true;
          break;
        case D:
          inputHandler.rightKey = true;
          inputHandler.keyPressed = true;
          break;
        case SPACE:
          inputHandler.jumpKey = true;
          inputHandler.keyPressed = true;
          break;
        default:
      }
    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      switch (event.getCode()) {
        case A:
          inputHandler.leftKey = false;
          inputHandler.keyPressed = false;
          break;
        case D:
          inputHandler.rightKey = false;
          inputHandler.keyPressed = false;
          break;
        case SPACE:
          inputHandler.jumpKey = false;
          inputHandler.keyPressed = false;
          break;
        default:
      }
    }
  }
}
