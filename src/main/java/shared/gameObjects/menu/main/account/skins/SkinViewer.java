package shared.gameObjects.menu.main.account.skins;

import client.handlers.userData.AccountData;
import client.main.Settings;
import java.io.File;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

/**
 * Menu item to view all skins in the game (used in Shop and Skin Selector)
 */
public class SkinViewer extends GameObject {

  private boolean init;
  private boolean[] skinData;
  private transient ImageView[] model;
  private transient Text text;
  private CycleManager cycleManager;

  /** Constructor */
  public SkinViewer(double x, double y, double sizeX, double sizeY, ObjectType id, UUID uuid) {
    super(x, y, sizeX, sizeY, id, uuid);
    init = false;
    model = new ImageView[8];
    skinData = new boolean[AccountData.SKIN_COUNT];
  }

  @Override
  public void update() {
    if(!init) {
      init = true;
      getAvailableSkins();
      ButtonCycle left, right;
      left = new ButtonCycle(getX() - 120, getY() + 120, 60, 40, false, ObjectType.Button, UUID.randomUUID());
      right = new ButtonCycle(getX() + 100, getY() + 120, 60, 40, true, ObjectType.Button, UUID.randomUUID());
      cycleManager = new CycleManager(getX() - 10, getY() + 160, 40, 40, left, right, "Skins", ObjectType.Button, UUID.randomUUID());
      settings.getLevelHandler().addGameObject(left);
      settings.getLevelHandler().addGameObject(right);
      settings.getLevelHandler().addGameObject(cycleManager);
    }

    int clickID = cycleManager.getClickID();
    if(clickID != 0) {
      int newSkin  = 0;
      cycleManager.setTextCount(newSkin = (clickID > 0 ? getNextSkinID(cycleManager.getTextCount()) : getPreviousSkinID(cycleManager.getTextCount())));
      updateSkinModels(newSkin);
      text.setText(skinData[newSkin] || newSkin == 0 ? "" : "LOCKED");
    }
  }

  private void getAvailableSkins() {
    skinData = new boolean[AccountData.SKIN_COUNT];
    for (int i = 0; i < skinData.length; i++) {
      skinData[i] = settings.getData().getSkins()[i];
    }
  }

  //Given an index, returns the next unlocked index
  private int getNextSkinID(int id) {
    id++;
      if(id >= skinData.length) id = 0;
    return id;
  }

  //Given an index, returns the previous unlocked index
  private int getPreviousSkinID(int id) {
    id--;
    if(id < 0) id = (skinData.length - 1);
    return id;
  }

  @Override
  public void initialiseAnimation() {}

  @Override
  public void render() {
    super.render();
    model[0].setTranslateX(getX() + 17); //Head
    model[0].setTranslateY(getY() + 13);
    model[0].setFitWidth(48);
    model[0].setFitHeight(58);
    model[0].toBack();

    model[1].setTranslateX(getX() + 22); //Body
    model[1].setTranslateY(getY() + 64);
    model[1].setFitWidth(39);
    model[1].setFitHeight(31);
    model[1].toBack();

    model[2].setTranslateX(getX() + 53); //ArmRight
    model[2].setTranslateY(getY() + 62);
    model[2].setFitWidth(17);
    model[2].setFitHeight(33);
    model[2].toFront();

    model[3].setTranslateX(getX() + 56); //HandRight
    model[3].setTranslateY(getY() + 82);
    model[3].setFitWidth(17);
    model[3].setFitHeight(15);
    model[3].toFront();

    model[4].setTranslateX(getX() + 13); //ArmLeft
    model[4].setTranslateY(getY() + 62);
    model[4].setFitWidth(17);
    model[4].setFitHeight(33);
    model[4].toFront();

    model[5].setTranslateX(getX() + 10); //HandLeft
    model[5].setTranslateY(getY() + 82);
    model[5].setFitWidth(17);
    model[5].setFitHeight(15);
    model[5].toFront();

    model[6].setTranslateX(getX() + 21); //LegRight
    model[6].setTranslateY(getY() + 89);
    model[6].setFitWidth(21);
    model[6].setFitHeight(23);
    model[6].toBack();

    model[7].setTranslateX(getX() + 45); //LegLeft
    model[7].setTranslateY(getY() + 89);
    model[7].setFitWidth(21);
    model[7].setFitHeight(23);
    model[7].toBack();

  }

  private void updateSkinModels(int skinID) {
    String start = "images/player/skin".replace('/', File.separatorChar);
    String end = ".png";
    model[0].setImage(new Image(start + skinID + File.separator + "head" + end));
    model[1].setImage(new Image(start + skinID + File.separator + "body" + end));
    model[2].setImage(new Image(start + skinID + File.separator + "arm" + end));
    model[3].setImage(new Image(start + skinID + File.separator + "hand" + end));
    model[4].setImage(new Image(start + skinID + File.separator + "arm" + end));
    model[5].setImage(new Image(start + skinID + File.separator + "hand" + end));
    model[6].setImage(new Image(start + skinID + File.separator + "leg" + end));
    model[7].setImage(new Image(start + skinID + File.separator + "leg" + end));
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    model = new ImageView[8];
    for (int i = 0; i < model.length; i++) {
      model[i] = new ImageView();
      model[i].setTranslateZ(-i);
    }
    text = new Text(getX() - 30, getY() + 210, "");
    text.setFont(settings.getFont(30));
    updateSkinModels(0);
    root.getChildren().addAll(model);
    root.getChildren().add(text);
  }

  @Override
  public void removeRender() {
    root.getChildren().removeAll(model);
    root.getChildren().remove(text);
    super.removeRender();
  }
}