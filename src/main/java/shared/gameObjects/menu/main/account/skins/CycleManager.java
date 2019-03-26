package shared.gameObjects.menu.main.account.skins;

import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

/**
 * Allows control of a cycle with two buttons
 */
public class CycleManager extends GameObject {

  private ButtonCycle left, right;
  private transient Text text;
  private String textContent;
  private int clickID;
  private int textCount;

  public CycleManager(double x, double y, double sizeX, double sizeY, ButtonCycle left, ButtonCycle right, String textContent, ObjectType id, UUID uuid) {
    super(x, y, sizeX, sizeY, id, uuid);
    this.left = left;
    left.setParent(this);
    this.right = right;
    right.setParent(this);
    this.textContent = textContent;
    textCount = 0;
  }

  protected void triggerClick(ButtonCycle cycle) {
    if (cycle.equals(right)) {
        clickID = 1;
    } else if (cycle.equals(left)) {
        clickID = -1;
    } else return;

  }

  public int getClickID() {
    int ret = clickID;
    clickID = 0;
    return ret;
  }

  @Override
  public void update() {
    super.update();
    text.setText(textContent); //Placing it here instead of in initialise avoids an unknown issue of persistant text between scenes
  }

  public int getTextCount() {
    return textCount;
  }

  public void setTextCount(int textCount) {
    this.textCount = textCount;
  }

  @Override
  public void initialiseAnimation() {

  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    text = new Text(getX(), getY(), "");
    text.setFill(Color.WHITE);
    text.setFont(settings.getFont(30));
    root.getChildren().add(text);
  }

  @Override
  public void removeRender() {
    super.removeRender();
    System.out.println(root.getChildren().contains(text));
    root.getChildren().remove(text);
    text = null;
  }
}
