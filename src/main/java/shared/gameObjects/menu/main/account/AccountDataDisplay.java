package shared.gameObjects.menu.main.account;

import client.handlers.accountHandler.AccountData;
import client.main.Settings;
import java.io.File;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

/** Displays the information on the current user account */
public class AccountDataDisplay extends GameObject {

  private transient Text uuid, username, achievements, skins, lootboxes, money;
  private transient ImageView trophy, skinImage, lootboxImage, moneyImage;
  private transient Rectangle box;

  public AccountDataDisplay(double x, double y, double sizeX, double sizeY, UUID uuid) {
    super(x, y, sizeX, sizeY, ObjectType.Button, uuid);
  }

  @Override
  public void update() {
    super.update();
    AccountData data = settings.getData();
    uuid.setText(data.getUUID());
    username.setText(data.getUsername());
    skins.setText("" + data.getSkinCount());
    achievements.setText("" + data.getAchievementCount());
    lootboxes.setText("" + data.getLootboxCount());
    money.setText("" + data.getMoneyCount());
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    String start = "images/account/".replace('/', File.separatorChar);
    String end = ".png";
    (uuid = new Text(getX() - 250, getY(), "")).setFont(settings.getFont(23));
    (username = new Text(getX() + 10, getY() + 50, "")).setFont(settings.getFont(26));

    (skins = new Text(getX() - 80, getY() + 100, "")).setFont(settings.getFont(26));
    skinImage = new ImageView(new Image(start + "skinIcon" + end));
    skinImage.setTranslateX(skins.getX() - 10);
    skinImage.setTranslateY(skins.getY() - 80);

    (achievements = new Text(getX() - 180, getY() + 100, "")).setFont(settings.getFont(26));
    trophy = new ImageView(new Image(start + "trophyIcon" + end));
    trophy.setTranslateX(achievements.getX() - 10);
    trophy.setTranslateY(achievements.getY() - 80);

    (lootboxes = new Text(getX() + 220, getY() + 100, "")).setFont(settings.getFont(26));
    lootboxImage = new ImageView(new Image(start + "lootboxIcon" + end));
    lootboxImage.setTranslateX(lootboxes.getX() - 10);
    lootboxImage.setTranslateY(lootboxes.getY() - 80);

    (money = new Text(getX() + 320, getY() + 100, "")).setFont(settings.getFont(26));
    moneyImage = new ImageView(new Image(start + "moneyIcon" + end));
    moneyImage.setTranslateX(money.getX() - 10);
    moneyImage.setTranslateY(money.getY() - 80);

    box = new Rectangle(getX() - 300, getY() - 30, 760, 160);
    box.setFill(Color.LIGHTGREY);
    box.setOpacity(0.8);
    box.setStroke(Color.BLACK);
    root.getChildren()
        .addAll(
            box,
            uuid,
            username,
            skins,
            skinImage,
            achievements,
            trophy,
            lootboxes,
            lootboxImage,
            money,
            moneyImage);
  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren()
        .removeAll(
            uuid,
            username,
            skins,
            skinImage,
            achievements,
            trophy,
            lootboxes,
            lootboxImage,
            money,
            moneyImage,
            box);
  }
}
