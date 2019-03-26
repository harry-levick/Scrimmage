package shared.gameObjects.menu.main.account.skins;

import client.handlers.userData.AccountData;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

/**
 * Menu item container and manager for Skin Selection (client-side)
 */
public class SkinSelector extends GameObject {

  private CycleManager[] skinCycles;
  private boolean init;
  private boolean[] skinData;

  /**
   * Constructor
   */
  public SkinSelector(double x, double y, double sizeX, double sizeY, ObjectType id, UUID uuid) {
    super(x, y, sizeX, sizeY, id, uuid);
    skinCycles = new CycleManager[4];
  }

  @Override
  public void initialiseAnimation() {

  }

  @Override
  public void update() {
    super.update();
    if(!init) {
      initSkinButton();
      getAvailableSkins();
    }
    boolean updateSkins = false;
    int[] newSkin = new int[4];
    for (int i = 0; i < 4; i++) {
      newSkin[i] = settings.getData().getActiveSkin()[i];
      int clickID = skinCycles[i].getClickID();
      if(clickID != 0) {
        updateSkins = true;
        skinCycles[i].setTextCount(newSkin[i] = (clickID > 0 ? getNextSkinID(skinCycles[i].getTextCount()) : getPreviousSkinID(skinCycles[i].getTextCount())));
      }

    }
    if(updateSkins) {
      settings.getData().applySkin(newSkin[0], newSkin[1], newSkin[2], newSkin[3]);
      settings.getLevelHandler().getClientPlayer().updateSkinRender(newSkin);
    }
  }

  //Given an index, returns the next unlocked index
  private int getNextSkinID(int id) {
    id++;
    for (int j = 0; j < skinData.length; j++) {
      if(id >= skinData.length) id = 0;
      if(skinData[id]) return id;
      id++;
    }
    return 0;
  }

  //Given an index, returns the previous unlocked index
  private int getPreviousSkinID(int id) {
    id--;
    for (int j = 0; j < skinData.length; j++) {
      if(id < 0) id = (skinData.length - 1);
      if(skinData[id]) return id;
      id--;
    }
    return 0;
  }

  //Gets unlocked skins from AccountData
  private void getAvailableSkins() {
   skinData = new boolean[AccountData.SKIN_COUNT];
   for (int i = 0; i < skinData.length; i++) {
     skinData[i] = settings.getData().getSkins()[i];
   }
   for (int i = 0; i < 4; i++) {
     skinCycles[i].setTextCount( settings.getData().getActiveSkin()[i]);
   }
  }
  //Initializes the button objects
  private void initSkinButton() {
    init = true;
    ButtonCycle left, right;
    left = new ButtonCycle(getX(), getY() + 40, 60, 40, false, ObjectType.Button, UUID.randomUUID());
    right = new ButtonCycle(getX() + 300, getY() + 40, 60, 40, true, ObjectType.Button, UUID.randomUUID());
    skinCycles[0] = new CycleManager(getX() + 152, getY() + 80, 40, 40, left, right, "Head", ObjectType.Button, UUID.randomUUID());
    settings.getLevelHandler().addGameObject(left);
    settings.getLevelHandler().addGameObject(right);
    settings.getLevelHandler().addGameObject(skinCycles[0]);

    left = new ButtonCycle(getX(), getY() + 120, 60, 40, false, ObjectType.Button, UUID.randomUUID());
    right = new ButtonCycle(getX() + 300, getY() + 120, 60, 40, true, ObjectType.Button, UUID.randomUUID());
    skinCycles[1] = new CycleManager(getX() + 152, getY() + 160, 40, 40, left, right, "Body", ObjectType.Button, UUID.randomUUID());
    settings.getLevelHandler().addGameObject(left);
    settings.getLevelHandler().addGameObject(right);
    settings.getLevelHandler().addGameObject(skinCycles[1]);

    left = new ButtonCycle(getX(), getY() + 200, 60, 40, false, ObjectType.Button, UUID.randomUUID());
    right = new ButtonCycle(getX() + 300, getY() + 200, 60, 40, true, ObjectType.Button, UUID.randomUUID());
    skinCycles[2] = new CycleManager(getX() + 152, getY() + 240, 40, 40, left, right, "Arm", ObjectType.Button, UUID.randomUUID());
    settings.getLevelHandler().addGameObject(left);
    settings.getLevelHandler().addGameObject(right);
    settings.getLevelHandler().addGameObject(skinCycles[2]);

    left = new ButtonCycle(getX(), getY() + 280, 60, 40, false, ObjectType.Button, UUID.randomUUID());
    right = new ButtonCycle(getX() + 300, getY() + 280, 60, 40, true, ObjectType.Button, UUID.randomUUID());
    skinCycles[3] = new CycleManager(getX() + 152, getY() + 320, 40, 40, left, right, "Leg", ObjectType.Button, UUID.randomUUID());
    settings.getLevelHandler().addGameObject(left);
    settings.getLevelHandler().addGameObject(right);
    settings.getLevelHandler().addGameObject(skinCycles[3]);
  }
}
