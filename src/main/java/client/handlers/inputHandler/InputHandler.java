package client.handlers.inputHandler;

public class InputHandler {

  public static boolean keyPressed;
  public static boolean leftKey, rightKey, jumpKey, click;
  public static double x, y;

  public InputHandler() {
    this.keyPressed = false;
    this.leftKey = false;
    this.rightKey = false;
    this.jumpKey = false;
    this.click = false;
  }

}
