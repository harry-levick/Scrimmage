package shared.gameObjects.menu.main.account;

import client.handlers.accountHandler.AccountData;
import client.handlers.accountHandler.AchivementHandler;
import client.handlers.accountHandler.Lootbox;
import client.handlers.accountHandler.SQLConnect;
import client.handlers.audioHandler.AudioHandler;
import client.handlers.effectsHandler.emitters.CircleEmitter;
import client.main.Client;
import client.main.Settings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.util.UUID;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.handlers.levelHandler.Map;
import shared.physics.Physics;
import shared.util.Path;
import shared.util.maths.Vector2;

public class AccountPageHandler extends GameObject {

  private transient Pane[] panes;
  private transient JFXButton[] buttons;
  private int currentPage;
  private int currentSkinID;

  public AccountPageHandler(UUID uuid) {
    super(0, 0, 1, 1, ObjectType.Button, uuid);
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    initButtons();
    initPanes();
    root.getChildren().add(panes[0]);
  }

  @Override
  public void update() {
    super.update();
    buttons[currentPage].setTextFill(Color.BLACK);
  }

  private void initButtons() {
    buttons = new JFXButton[6];

    // Details Button
    buttons[0] = new JFXButton("Account");
    buttons[1] = new JFXButton("Skins");
    buttons[2] = new JFXButton("Trophies");
    buttons[3] = new JFXButton("Lootbox");
    buttons[4] = new JFXButton("Shop");
    buttons[5] = new JFXButton("Main Menu");

    for (int i = 0; i < buttons.length; i++) {
      final int temp = i;
      buttons[i].setFont(settings.getFont(42));
      buttons[i].setPrefWidth(340);
      buttons[i].setTranslateX(40 + 40 * i + i * 335);
      buttons[i].setTranslateY(120);
      buttons[i].setRipplerFill(Color.LIGHTBLUE);
      buttons[i].setTextFill(Color.WHITE);
      buttons[i].setButtonType(ButtonType.RAISED);
      buttons[i].setOnMousePressed(event -> doClickMenu(event, temp));
      buttons[i].setOnMouseEntered(event -> buttons[temp].setTextFill(Color.LIGHTBLUE));
      buttons[i].setOnMouseExited(event -> buttons[temp].setTextFill(Color.WHITE));
    }

    buttons[5].setTranslateX(buttons[2].getTranslateX() - 20);
    buttons[5].setTranslateY(60);
    buttons[5].setPrefWidth(380);

    root.getChildren().addAll(buttons);
    buttons[0].setTextFill(Color.BLACK);
  }

  private void doClickMenu(MouseEvent e, int id) {
    new AudioHandler(settings, Client.musicActive).playSFX("CLICK");
    if(id == 5) {
      settings.getLevelHandler().changeMap(
          new Map("menus/main_menu.map", Path.convert("src/main/resources/menus/main_menu.map")),
          true, false);
    }
    settings
        .getLevelHandler()
        .addGameObject(
            new CircleEmitter(
                new Vector2(
                    e.getX() + buttons[id].getTranslateX(), e.getY() + buttons[id].getTranslateY()),
                new Vector2(100, 100),
                Vector2.Zero(),
                new Vector2(12, 12),
                15,
                0.30f,
                Physics.TIMESTEP,
                12,
                false,
                Path.convert("images/particle/BulletParticle.png")));
    if (currentPage == id) return;
    else {
      root.getChildren().remove(panes[currentPage]);
      initPanes();
      root.getChildren().add(panes[id]);
      buttons[id].setTextFill(Color.BLACK);
      buttons[currentPage].setTextFill(Color.WHITE);
      currentPage = id;
    }
  }

  private void sqlResponse(String username, String password, Label[] labels, Label notice, int id) {
    String ret;
    switch (id) {
      case 0:
        settings.getData().setUsername(username.toUpperCase());
         ret = SQLConnect.saveData(settings.getData());
         if (ret.startsWith("new")) {
           notice.setText("Please Login or Register before updating");
           notice.setTextFill(Color.ORANGE);
         }
         else if(ret.startsWith("fail")) {
           notice.setText("Error connecting - try again later");
           notice.setTextFill(Color.RED);
         } else {
           notice.setText("Updated username");
           notice.setTextFill(Color.GREEN);
         }
        break;
      case 1:
         ret = SQLConnect.getUserdata(username.toUpperCase(), password);
        if(ret.startsWith("fail")) {
          notice.setText("User/password not found");
          notice.setTextFill(Color.RED);
        } else {
          settings.setData(AccountData.fromString(ret));
          AccountData data = settings.getData();
          labels[0].setText(data.getUUID());
          labels[1].setText(data.getUsername());
          labels[2].setText("Skins - " + data.getSkinCount());
          labels[3].setText("Trophies - " + data.getAchievementCount());
          labels[4].setText("Boxes - " + data.getLootboxCount());
          labels[5].setText("Scrimbucks - " + data.getMoneyCount());
          notice.setText("Logged In");
          notice.setTextFill(Color.GREEN);
        }
        break;
      case 2:
        settings.getData().setUsername(username.toUpperCase());
        ret = SQLConnect.registerUser(settings.getData(), password);
        if(ret.startsWith("exists")) {
          notice.setText("A user with that name already exists.");
          notice.setTextFill(Color.ORANGE);
        } else if (ret.startsWith("fail")) {
          notice.setText("Error connecting to server");
          notice.setTextFill(Color.RED);
        }
        else {
          notice.setText("Successfully Registered");
          notice.setTextFill(Color.GREEN);
        }
        break;
    }
  }

  //Given an index, returns the next unlocked index
  private int getNextSkinViewerID() {
    currentSkinID++;
    if(currentSkinID >= AccountData.SKIN_COUNT) currentSkinID = 0;
    return currentSkinID;
  }

  //Given an index, returns the previous unlocked index
  private int getPreviousSkinViewerID() {
    currentSkinID--;
    if(currentSkinID < 0) currentSkinID = (AccountData.SKIN_COUNT - 1);
    return currentSkinID;
  }

  //Given an index, returns the next unlocked index
  private int getNextSkinID(int id) {
    id++;
    for (int j = 0; j < AccountData.SKIN_COUNT ; j++) {
      if(id >= AccountData.SKIN_COUNT ) id = 0;
      if(settings.getData().hasSkin(id)) return id;
      id++;
    }
    return 0;
  }

  //Given an index, returns the previous unlocked index
  private int getPreviousSkinID(int id) {
    id--;
    for (int j = 0; j < AccountData.SKIN_COUNT ; j++) {
      if(id < 0) id = (AccountData.SKIN_COUNT  - 1);
      if(settings.getData().hasSkin(id)) return id;
      id--;
    }
    return 0;
  }

  private void applyNewSkin(int id, boolean isNext) {
    new AudioHandler(settings, Client.musicActive).playSFX("CLICK");
    int[] currentSkin = settings.getData().getActiveSkin();
    currentSkin[id] = isNext ? getNextSkinID(currentSkin[id]) : getPreviousSkinID(currentSkin[id]);
    settings.getData().applySkin(currentSkin);
    settings.getLevelHandler().getClientPlayer().updateSkinRender(currentSkin);
  }
  private void renderSkinViewer(int currentSkinID, ImageView[] model) {
    String start = "images/player/skin".replace('/', File.separatorChar);
    String end = ".png";
    model[0].setImage(new Image(start + currentSkinID + File.separator + "head" + end));
    model[1].setImage(new Image(start + currentSkinID + File.separator + "body" + end));
    model[2].setImage(new Image(start + currentSkinID + File.separator + "arm" + end));
    model[3].setImage(new Image(start + currentSkinID + File.separator + "hand" + end));
    model[4].setImage(new Image(start + currentSkinID + File.separator + "arm" + end));
    model[5].setImage(new Image(start + currentSkinID + File.separator + "hand" + end));
    model[6].setImage(new Image(start + currentSkinID + File.separator + "leg" + end));
    model[7].setImage(new Image(start + currentSkinID + File.separator + "leg" + end));
    if(settings.getData().hasSkin(currentSkinID)) model[8].setImage(new Image(Path.convert("images/blank.png")));
    else model[8].setImage(new Image(Path.convert("images/ui/icons/locked.png")));
  }

  private void openLootbox(Label lootboxStatus, Label notification) {
    new AudioHandler(settings, Client.musicActive).playSFX("CLICK");
    if(settings.getData().getLootboxCount() <= 0)  {
      notification.setText("No Lootboxes Available");
      notification.setTextFill(Color.RED);
    } else {
      notification.setText(Lootbox.rollLootbox(settings.getData()));
      notification.setTextFill(Color.GREEN);
      lootboxStatus.setText(settings.getData().getLootboxCount() + " Box(es) Remaining");
    }
  }

  private void purchaseBox(int id, Label notification, Label status) {
    if(settings.getData().getMoneyCount() < Lootbox.LOOTBOX_PRICE) {
      notification.setText("Not enough scrimbucks to purchase.");
      notification.setTextFill(Color.RED);
    } else {
      if(id == 0) {
        settings.getData().earnLootbox();
        settings.getData().removeMoney(Lootbox.LOOTBOX_PRICE);
        notification.setText("Purchased 1 Lootbox");
        notification.setTextFill(Color.GREEN);
      } else {
        if(settings.getData().getMoneyCount() < Lootbox.LOOTBOX_PRICE*5) {
          notification.setText("Not enough scrimbucks to purchase.");
          notification.setTextFill(Color.RED);
        } else
        {
          for(int i = 0; i < 5; i++) settings.getData().earnLootbox();
          settings.getData().removeMoney(Lootbox.LOOTBOX_PRICE*5);
          notification.setText("Purchased 5 Lootbox");
          notification.setTextFill(Color.GREEN);
        }
      }
    }
    status.setText(settings.getData().getMoneyCount() + " Scrimbucks");
  }

  private void processCode(String code, Label notification, Label status) {

  }

  private void initPanes() {
    panes = new Pane[5];
    initAccountPane();
    initSkinPane();
    initTropyPane();
    initLootboxPane();
    initShopPane();
  }

  private void initAccountPane() {
    panes[0] = new Pane();
    AccountData data = settings.getData();
    Label[] labels = new Label[6];
    labels[0] = new Label(data.getUUID());
    labels[1] = new Label(data.getUsername());
    labels[2] = new Label("Skins - " + data.getSkinCount());
    labels[3] = new Label("Trophies - " + data.getAchievementCount());
    labels[4] = new Label("Boxes - " + data.getLootboxCount());
    labels[5] = new Label("Scrimbucks - " + data.getMoneyCount());

    for (int i = 0; i < labels.length; i++) {
      labels[i].setFont(settings.getFont(34));
      labels[i].setPrefWidth(1100);
      labels[i].setTextFill(Color.BLACK);
      labels[i].relocate(0, 60 * i);
      labels[i].setAlignment(Pos.CENTER);
    }

    JFXTextField username = new JFXTextField("username");
    JFXPasswordField passwd = new JFXPasswordField();
    username.setPrefSize(400, 60);
    username.relocate(350, 440);
    username.setFont(settings.getFont(28));
    username.setAlignment(Pos.BASELINE_LEFT);

    passwd.setPrefSize(400, 60);
    passwd.relocate(350, 520);
    passwd.setFont(settings.getFont(28));
    passwd.setAlignment(Pos.BASELINE_LEFT);

    JFXButton[] registration = new JFXButton[3];
    registration[0] = new JFXButton("Update");
    registration[1] = new JFXButton("Login");
    registration[2] = new JFXButton("Register");

    for (int i = 0; i < 3; i++) {
      final int temp = i;
      registration[i].relocate(350*i + 40, 640);
      registration[i].setFont(settings.getFont(28));
      registration[i].setPrefWidth(300);
      registration[i].setTextFill(Color.WHITE);
      registration[i].setOnMouseEntered(event -> registration[temp].setTextFill(Color.DARKBLUE));
      registration[i].setOnMouseExited(event -> registration[temp].setTextFill(Color.WHITE));
    }

    Label notice = new Label("");
    notice.setFont(settings.getFont(28));
    notice.setAlignment(Pos.CENTER);
    notice.relocate(0, 720);
    notice.setPrefWidth(1100);

    registration[0].setOnMouseClicked((event -> sqlResponse(username.getText(), passwd.getText(), labels,notice, 0)));
    registration[1].setOnMouseClicked((event -> sqlResponse(username.getText(), passwd.getText(), labels,notice,1)));
    registration[2].setOnMouseClicked((event -> sqlResponse(username.getText(), passwd.getText(), labels,notice,2)));

    panes[0].getChildren().addAll(labels);
    panes[0].getChildren().addAll(registration);
    panes[0].getChildren().add(username);
    panes[0].getChildren().add(passwd);
    panes[0].getChildren().add(notice);
    panes[0].setTranslateX(1920 / 4 - 60);
    panes[0].setTranslateY(200);
  }

  private void initSkinPane() {
    panes[1] = new Pane();
    //Skin Selector Arrow Keys
    JFXButton[] skinSelector = new JFXButton[8];
    (skinSelector[0] = new JFXButton("-|>")).setOnMouseClicked(event -> applyNewSkin(0, true));
    (skinSelector[1] = new JFXButton("-|>")).setOnMouseClicked(event -> applyNewSkin(1, true));
    (skinSelector[2] = new JFXButton("-|>")).setOnMouseClicked(event -> applyNewSkin(2, true));
    (skinSelector[3] = new JFXButton("-|>")).setOnMouseClicked(event -> applyNewSkin(3, true));
    (skinSelector[4] = new JFXButton("<|-")).setOnMouseClicked(event -> applyNewSkin(0, false));
    (skinSelector[5] = new JFXButton("<|-")).setOnMouseClicked(event -> applyNewSkin(1, false));
    (skinSelector[6] = new JFXButton("<|-")).setOnMouseClicked(event -> applyNewSkin(2, false));
    (skinSelector[7] = new JFXButton("<|-")).setOnMouseClicked(event -> applyNewSkin(3, false));

    //Skin Selector Title Keys
    Label[] titles = new Label[5];
    titles[0] = new Label("Change Skin");
    titles[1] = new Label("Head");
    titles[2] = new Label("Body");
    titles[3] = new Label("Arms");
    titles[4] = new Label("Legs");

    for (int i = 0; i < titles.length; i++) {
      titles[i].setFont(settings.getFont(38));
      titles[i].relocate(i == 0 ? 120 : 200, 82*(i+1));
      titles[i].setTextFill(Color.BLACK);
      titles[i].setAlignment(Pos.CENTER);
    }
    titles[0].setTextFill(Color.BLACK);

    for (int i = 0; i < skinSelector.length; i++) {
      final int temp = i;
      skinSelector[i].setPrefWidth(120);
      skinSelector[i].setTextFill(Color.WHITE);
      skinSelector[i].setFont(settings.getFont(38));
      skinSelector[i].setAlignment(Pos.CENTER);
      skinSelector[i].setOnMouseEntered(event -> skinSelector[temp].setTextFill(Color.LIGHTBLUE));
      skinSelector[i].setOnMouseExited(event -> skinSelector[temp].setTextFill(Color.WHITE));
      if(i < 4) {
        skinSelector[i].relocate(400, 80*i + 160);
      } else {
        skinSelector[i].relocate(0, 80*(i - 4) + 160);
      }
    }

    //SkinViewer
    Group viewer = new Group();
    final ImageView[] model = new ImageView[9];


    String start = "images/player/skin".replace('/', File.separatorChar);
    String end = ".png";
    model[0] = new ImageView(new Image(start + 0 + File.separator + "head" + end));
    model[1] = new ImageView(new Image(start + 0 + File.separator + "body" + end));
    model[2] = new ImageView(new Image(start + 0 + File.separator + "arm" + end));
    model[3] = new ImageView(new Image(start + 0 + File.separator + "hand" + end));
    model[4] = new ImageView(new Image(start + 0 + File.separator + "arm" + end));
    model[5] = new ImageView(new Image(start + 0 + File.separator + "hand" + end));
    model[6] = new ImageView(new Image(start + 0 + File.separator + "leg" + end));
    model[7] = new ImageView(new Image(start + 0 + File.separator + "leg" + end));
    model[8] = new ImageView(new Image(Path.convert("images/blank.png")));

    viewer.getChildren().addAll(model);
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

    model[8].setTranslateX(-10);
    model[8].setTranslateY(15);
    model[8].toFront();

    viewer.setScaleX(2.2);
    viewer.setScaleY(2.2);
    viewer.relocate(1300, 300);

    JFXButton[] viewerCycle = new JFXButton[2];
    (viewerCycle[0] = new JFXButton("<|-")).setOnMouseClicked(event -> renderSkinViewer(getPreviousSkinViewerID(), model));
    (viewerCycle[1] = new JFXButton("-|>")).setOnMouseClicked(event -> renderSkinViewer(getNextSkinViewerID(), model));
    for (int i = 0; i < 2; i++) {
      final int temp = i;
    viewerCycle[i].relocate(1222 + 145*i, 150);
    viewerCycle[i].setTextFill(Color.BLACK);
    viewerCycle[i].setFont(settings.getFont(42));
    viewerCycle[i].setPrefWidth(130);
    viewerCycle[i].setAlignment(Pos.CENTER);
      viewerCycle[i].setOnMouseEntered(event -> viewerCycle[temp].setTextFill(Color.LIGHTBLUE));
      viewerCycle[i].setOnMouseExited(event -> viewerCycle[temp].setTextFill(Color.BLACK));
    }

    Label title = new Label("View Skins");
    title.setFont(settings.getFont(38));
    title.relocate(1222, 82);
    title.setTextFill(Color.BLACK);
    title.setAlignment(Pos.CENTER);

    Label skinCount = new Label(settings.getData().getSkinCount() + " / " + AccountData.SKIN_COUNT + " Skins");

    skinCount.relocate(440, 40);
    skinCount.setTextFill(Color.BLACK);
    skinCount.setPrefWidth(800);
    skinCount.setFont(settings.getFont(52));
    skinCount.setAlignment(Pos.CENTER);

    panes[1].getChildren().add(viewer);
    panes[1].getChildren().addAll(viewerCycle);
    panes[1].getChildren().addAll(skinSelector);
    panes[1].getChildren().addAll(titles);
    panes[1].getChildren().add(title);
    panes[1].getChildren().add(skinCount);
    panes[1].setTranslateX(100);
    panes[1].setTranslateY(180);
  }

  private void initTropyPane() {
    panes[2] = new Pane();
    Group group = new Group();
    AchivementHandler achivementHandler = new AchivementHandler(UUID.randomUUID());
    achivementHandler.initialise(group, settings);

    Label trophyCount = new Label(settings.getData().getAchievementCount() + " / 24 Trophies Earned");
    trophyCount.relocate(540, 40);
    trophyCount.setTextFill(Color.BLACK);
    trophyCount.setFont(settings.getFont(52));
    trophyCount.setPrefWidth(800);
    trophyCount.setAlignment(Pos.CENTER);

    group.setScaleX(0.85);
    group.setScaleY(0.85);

    panes[2].getChildren().add(trophyCount);
    panes[2].getChildren().add(group);
    panes[2].setTranslateX(0);
    panes[2].setTranslateY(180);
  }

  private void initLootboxPane() {
    panes[3] = new Pane();
    Label lootboxStatus = new Label(settings.getData().getLootboxCount() + " Box(es) Remaining");
    lootboxStatus.relocate(540, 20);
    lootboxStatus.setTextFill(Color.BLACK);
    lootboxStatus.setPrefWidth(800);
    lootboxStatus.setFont(settings.getFont(52));
    lootboxStatus.setAlignment(Pos.CENTER);

    Label notification = new Label("");
    notification.relocate(540, 440);
    notification.setTextFill(Color.BLACK);
    notification.setPrefWidth(800);
    notification.setFont(settings.getFont(32));
    notification.setAlignment(Pos.CENTER);

    JFXButton openBox = new JFXButton("Open Box");
    openBox.relocate(540, 540);
    openBox.setTextFill(Color.WHITE);
    openBox.setFont(settings.getFont(42));
    openBox.setPrefWidth(800);
    openBox.setAlignment(Pos.CENTER);
    openBox.setOnMouseClicked(event -> openLootbox(lootboxStatus, notification));
    openBox.setOnMouseEntered(event -> openBox.setTextFill(Color.DARKBLUE));
    openBox.setOnMouseExited(event -> openBox.setTextFill(Color.WHITE));


    panes[3].getChildren().add(openBox);
    panes[3].getChildren().add(lootboxStatus);
    panes[3].getChildren().add(notification);
    panes[3].setTranslateX(0);
    panes[3].setTranslateY(200);
  }

  private void initShopPane() {
    panes[4] = new Pane();

    Label moneyStatus = new Label(settings.getData().getMoneyCount() + " Scrimbucks");
    moneyStatus.relocate(540, 20);
    moneyStatus.setTextFill(Color.BLACK);
    moneyStatus.setPrefWidth(800);
    moneyStatus.setFont(settings.getFont(52));
    moneyStatus.setAlignment(Pos.CENTER);

    Label notification = new Label("");
    notification.relocate(340, 600);
    notification.setTextFill(Color.BLACK);
    notification.setPrefWidth(1200);
    notification.setFont(settings.getFont(32));
    notification.setAlignment(Pos.CENTER);

    JFXButton[] purchase = new JFXButton[2];
    purchase[0] = new JFXButton("Purchase");
    purchase[1] = new JFXButton("Purchase x5");
    for (int i=0; i < purchase.length; i++) {
      final int temp = i;
      purchase[i].setFont(settings.getFont(42));
      purchase[i].setPrefWidth(560);
      purchase[i].setTranslateX(80);
      purchase[i].setTranslateY(200 + 120*i);
      purchase[i].setTextFill(Color.WHITE);
      purchase[i].setOnMousePressed(event -> purchaseBox(temp, notification, moneyStatus));
      purchase[i].setOnMouseEntered(event -> purchase[temp].setTextFill(Color.LIGHTBLUE));
      purchase[i].setOnMouseExited(event -> purchase[temp].setTextFill(Color.WHITE));
    }
    JFXTextField inputCode = new JFXTextField("Enter Code Here");
    inputCode.setFont(settings.getFont(28));
    inputCode.setPrefWidth(560);
    inputCode.relocate(1240, 200);

    JFXButton validateCode = new JFXButton("Enter");
    validateCode.setFont(settings.getFont(42));
    validateCode.setPrefWidth(560);
    validateCode.relocate(1240, 320);
    validateCode.setTextFill(Color.WHITE);
    validateCode.setOnMousePressed(event -> processCode(inputCode.getText().toLowerCase(), notification, moneyStatus));
    validateCode.setOnMouseEntered(event -> validateCode.setTextFill(Color.LIGHTBLUE));
    validateCode.setOnMouseExited(event -> validateCode.setTextFill(Color.WHITE));

    panes[4].getChildren().addAll(purchase);
    panes[4].getChildren().add(notification);
    panes[4].getChildren().add(validateCode);
    panes[4].getChildren().add(inputCode);
    panes[4].getChildren().add(moneyStatus);
    panes[4].setTranslateX(0);
    panes[4].setTranslateY(200);
  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().removeAll(buttons);
    root.getChildren().removeAll(panes);
  }
}
