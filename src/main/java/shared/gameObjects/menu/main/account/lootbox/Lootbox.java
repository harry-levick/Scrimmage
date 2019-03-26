package shared.gameObjects.menu.main.account.lootbox;

import client.main.Settings;
import com.sun.org.apache.bcel.internal.generic.FLOAD;
import java.util.Random;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.swing.text.html.ImageView;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.main.account.skins.ButtonBasic;
import shared.gameObjects.menu.main.account.skins.CycleManager;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class Lootbox extends GameObject {

  private static final float SKIN_CHANCE = 0.25f;
  private static final float RARE_SKIN_CHANCE = 0.25f;
  private static final float LARGE_MONEY_CHANCE = 0.15f;
  private static final float MEDIUM_MONEY_CHANCE = 0.2975f;
  private static final int SMALL_MONEY = 50;
  private static final int MEDIUM_MONEY = 150;
  private static final int LARGE_MONEY = 450;

  private CycleManager cycleManager;
  private boolean init;
  private boolean opening;

  private transient Text text;
  //TODO Add box image and particles
  private transient ImageView box;

  public Lootbox(double x, double y, double sizeX, double sizeY, UUID uuid) {
    super(x, y, sizeX, sizeY, ObjectType.Button, uuid);
  }

  @Override
  public void update() {
    super.update();
    if(!init) {
      init = true;
      ButtonBasic left, right;
      left = new ButtonBasic(getX() - 420, getY() + 220, 60*6, 40*2, "Account", ObjectType.Button, UUID.randomUUID());
      right = new ButtonBasic(getX() + 200, getY() + 220, 60*6, 40*2, "Open", ObjectType.Button, UUID.randomUUID());
      cycleManager = new CycleManager(getX() - 10, getY() + 160, 40, 40, left, right, "", ObjectType.Button, UUID.randomUUID());
      settings.getLevelHandler().addGameObject(left);
      settings.getLevelHandler().addGameObject(right);
      settings.getLevelHandler().addGameObject(cycleManager);
    }

    int clickID = cycleManager.getClickID();
    if(clickID != 0) {
      if(clickID > 0) {
        openLootBox();
      } else {
        returnToAccountMenu();
      }
    }
  }

  //Opens a loot box if not currently opening one
  private void openLootBox() {
    if(settings.getData().getLootboxCount() <= 0) return;
    new Thread(() -> {
        if(opening) return;
        opening = true;
        try {
          text.setText("");
          settings.getData().openLootbox();
          Thread.sleep(1000);
          switch (getRewardID()) {
            case 1:
              awardSkin(true);
              break;
            case 2:
              awardSkin(false);
              break;
            case 3:
              awardMoney(SMALL_MONEY);
              break;
            case 4:
              awardMoney(MEDIUM_MONEY);
              break;
            case 5:
              awardMoney(LARGE_MONEY);
              break;
          }
        } catch (Exception e) {

        }
        finally{
          opening =false;
        }
        //Open Lootbox
    }).start();
  }
  //Returns to Menu if not currently opening a box
  private void returnToAccountMenu() {
    if(opening) return;
    settings.getLevelHandler().changeMap(
        new Map(
            "ACCOUNT",
            Path.convert("src/main/resources/menus/account.map")),
        false, false);
  }

  private int getRewardID() {
    Random random = new Random();
    int id = 0;
    if(random.nextFloat() <= SKIN_CHANCE) {
      if(random.nextFloat() <= RARE_SKIN_CHANCE) id = 2;
      else id = 1;
    } else {
      if(random.nextFloat() <= LARGE_MONEY_CHANCE) id = 5;
      else {
        if(random.nextFloat() <= MEDIUM_MONEY_CHANCE) id = 4;
        else id = 3;
      }
    }
    return id;
  }

  private void awardMoney(int value) {
    text.setText("$" + value);
    text.setFill(Color.GREEN);
    settings.getData().addMoney(value);
  }

  public void awardSkin(boolean rare) {
    int skinID;
    text.setText("SKIN");
    text.setFill(Color.ORANGE);
    Random random = new Random();
    if(rare) {
      settings.getData().awardSkin(random.nextInt(2) + 5);
    } else {
      settings.getData().awardSkin(random.nextInt(5));
    }
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    (text = new Text(getX(), getY(), "")).setFont(settings.getFont(28));
    root.getChildren().add(text);
  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().remove(text);
  }
}
