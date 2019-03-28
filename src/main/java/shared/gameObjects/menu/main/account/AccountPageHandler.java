package shared.gameObjects.menu.main.account;

import client.handlers.accountHandler.AccountData;
import client.handlers.accountHandler.AchivementHandler;
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
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
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

  private void initButtons() {
    buttons = new JFXButton[5];

    // Details Button
    buttons[0] = new JFXButton("Account");
    buttons[1] = new JFXButton("Skins");
    buttons[2] = new JFXButton("Trophies");
    buttons[3] = new JFXButton("Lootbox");
    buttons[4] = new JFXButton("Shop");

    for (int i = 0; i < buttons.length; i++) {
      final int temp = i;
      buttons[i].setFont(settings.getFont(42));
      buttons[i].setPrefWidth(340);
      buttons[i].setTranslateX(40 + 40 * i + i * 335);
      buttons[i].setTranslateY(80);
      buttons[i].setRipplerFill(Color.LIGHTBLUE);
      buttons[i].setTextFill(Color.WHITE);
      buttons[i].setButtonType(ButtonType.RAISED);
      buttons[i].setOnMousePressed(event -> doClickMenu(event, temp));
    }

    root.getChildren().addAll(buttons);
    buttons[0].setTextFill(Color.BLACK);
  }

  private void doClickMenu(MouseEvent e, int id) {
    new AudioHandler(settings, Client.musicActive).playSFX("CLICK");
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
    switch (id) {
      case 0:

        break;
      case 1:
        String ret = SQLConnect.getUserdata(username, password);
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
        //User already exists, registered successfuly, Failed
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
      registration[i].relocate(350*i + 40, 640);
      registration[i].setFont(settings.getFont(28));
      registration[i].setPrefWidth(300);
      registration[i].setTextFill(Color.WHITE);
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
      titles[i].setTextFill(Color.WHITE);
      titles[i].setAlignment(Pos.CENTER);
    }
    titles[0].setTextFill(Color.BLACK);

    for (int i = 0; i < skinSelector.length; i++) {
      skinSelector[i].setPrefWidth(120);
      skinSelector[i].setTextFill(Color.BLACK);
      skinSelector[i].setFont(settings.getFont(38));
      skinSelector[i].setAlignment(Pos.CENTER);
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
    model[8] = new ImageView(new Image(Path.convert("images/ui/icons/locked.png")));

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
    viewerCycle[i].relocate(1222 + 145*i, 150);
    viewerCycle[i].setTextFill(Color.BLACK);
    viewerCycle[i].setFont(settings.getFont(42));
    viewerCycle[i].setPrefWidth(130);
    viewerCycle[i].setAlignment(Pos.CENTER);
    }

    Label title = new Label("View Skins");
    title.setFont(settings.getFont(38));
    title.relocate(1222, 82);
    title.setTextFill(Color.BLACK);
    title.setAlignment(Pos.CENTER);

    panes[1].getChildren().add(viewer);
    panes[1].getChildren().addAll(viewerCycle);
    panes[1].getChildren().addAll(skinSelector);
    panes[1].getChildren().addAll(titles);
    panes[1].getChildren().add(title);
    panes[1].setTranslateX(100);
    panes[1].setTranslateY(200);
  }

  private void initTropyPane() {
    panes[2] = new Pane();
    Group group = new Group();
    AchivementHandler achivementHandler = new AchivementHandler(UUID.randomUUID());
    achivementHandler.initialise(group, settings);

    group.setScaleX(0.85);
    group.setScaleY(0.85);
    group.setTranslateY(-100);
    panes[2].getChildren().add(group);
    panes[2].setTranslateX(0);
    panes[2].setTranslateY(200);
  }

  private void initLootboxPane() {
    panes[3] = new GridPane();
  }

  private void initShopPane() {
    panes[4] = new GridPane();
  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().removeAll(buttons);
    root.getChildren().removeAll(panes);
  }
}
