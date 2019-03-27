package shared.gameObjects.menu.main.account;

import client.handlers.accountHandler.AccountData;
import client.handlers.accountHandler.SQLConnect;
import client.handlers.audioHandler.AudioHandler;
import client.handlers.effectsHandler.emitters.CircleEmitter;
import client.main.Client;
import client.main.Settings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.util.UUID;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.physics.Physics;
import shared.util.Path;
import shared.util.maths.Vector2;

public class AccountPageHandler extends GameObject {

  private transient Group[] panes;
  private transient JFXButton[] buttons;
  private int currentPage;

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
      buttons[i].setOnMousePressed(event -> doOnClick(event, temp));
    }

    root.getChildren().addAll(buttons);
    buttons[0].setTextFill(Color.BLACK);
  }

  private void doOnClick(MouseEvent e, int id) {
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

  private void buttonResponse(String username, String password, Label[] labels, Label notice, int id) {
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

  private void initPanes() {
    panes = new Group[5];
    initAccountPane();
    initSkinPane();
    initTropyPane();
    initLootboxPane();
    initShopPane();
  }

  private void initAccountPane() {
    panes[0] = new Group();
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

    registration[0].setOnMouseClicked((event -> buttonResponse(username.getText(), passwd.getText(), labels,notice, 0)));
    registration[1].setOnMouseClicked((event -> buttonResponse(username.getText(), passwd.getText(), labels,notice,1)));
    registration[2].setOnMouseClicked((event -> buttonResponse(username.getText(), passwd.getText(), labels,notice,2)));

    panes[0].getChildren().addAll(labels);
    panes[0].getChildren().addAll(registration);
    panes[0].getChildren().add(username);
    panes[0].getChildren().add(passwd);
    panes[0].getChildren().add(notice);
    panes[0].setTranslateX(1920 / 4 - 60);
    panes[0].setTranslateY(200);
  }

  private void initSkinPane() {
    panes[1] = new Group();
  }

  private void initTropyPane() {
    panes[2] = new Group();
  }

  private void initLootboxPane() {
    panes[3] = new Group();
  }

  private void initShopPane() {
    panes[4] = new Group();
  }
}
