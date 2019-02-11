package client.handlers.inputHandler;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class MouseInput implements EventHandler<MouseEvent> {

  private InputHandler inputHandler;

  public MouseInput(InputHandler inputHandler) {
    this.inputHandler = inputHandler;
  }

  @Override
  public void handle(MouseEvent event) {
    EventType<MouseEvent> type = (EventType<MouseEvent>) event.getEventType();
    
    if (type == MouseEvent.MOUSE_MOVED || type == MouseEvent.MOUSE_DRAGGED) {
      inputHandler.x = event.getX();
      inputHandler.y = event.getY();
    }
    if (type == MouseEvent.MOUSE_PRESSED) {
      inputHandler.click = true;
    } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
      inputHandler.click = false;
    }
  }
}
