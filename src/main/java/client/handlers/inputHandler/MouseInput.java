package client.handlers.inputHandler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseInput implements EventHandler<MouseEvent> {

    public boolean click;
    public double x,y;

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            x = event.getX();
            y = event.getY();
        }
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            click = true;
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            click = false;
        }
    }
}
