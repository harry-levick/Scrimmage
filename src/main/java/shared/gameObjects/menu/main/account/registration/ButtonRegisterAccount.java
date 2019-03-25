package shared.gameObjects.menu.main.account.registration;

import client.handlers.userData.AccountData;
import client.handlers.userData.SQLConnect;
import client.main.Settings;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonRegisterAccount extends ButtonObject {
  private transient TextField usernameInput;
  private transient TextField passwordInput;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonRegisterAccount(double x, double y, double sizeX, double sizeY, ObjectType id,
      UUID objectUUID) {
    super(x, y, sizeX, sizeY, "Login", id, objectUUID);
  }

  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    usernameInput = new TextField();
    usernameInput.setTranslateX(getX() + 90);
    usernameInput.setTranslateY(getY() - 80);

    passwordInput = new TextField();
    passwordInput.setTranslateX(getX() + 90);
    passwordInput.setTranslateY(getY() - 20);

    root.getChildren().add(passwordInput);
    root.getChildren().add(usernameInput);
    //TODO note: this does not currently get removed by the gamObject clear when changing maps
  }


  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    System.out.println(settings.getData().getUsername());
   // String ret = SQLConnect.getUserdata(usernameInput.getText(), passwordInput.getText());
   // if(ret.startsWith("fail")) {
   //   System.out.println("Failed");
  //  } else {
  //    settings.setData(AccountData.fromString(ret));
  //    System.out.println(settings.getData().getUsername());
  //  }
  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().remove(usernameInput);
    root.getChildren().remove(passwordInput);
  }
}
