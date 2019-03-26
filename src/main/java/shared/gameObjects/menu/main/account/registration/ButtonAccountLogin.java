package shared.gameObjects.menu.main.account.registration;

import client.handlers.accountHandler.AccountData;
import client.handlers.accountHandler.SQLConnect;
import client.main.Settings;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

/**
 * Button handling the userinput for logging in to an account on the SQL
 */
public class ButtonAccountLogin extends ButtonObject {

  private transient JFXTextField usernameInput;
  private transient JFXPasswordField passwordInput;
  private transient Text text;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonAccountLogin(double x, double y, double sizeX, double sizeY, ObjectType id,
      UUID objectUUID) {
    super(x, y, sizeX, sizeY, "Login", id, objectUUID);
  }

  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    usernameInput = new JFXTextField();
    usernameInput.setTranslateX(getX() + 80);
    usernameInput.setTranslateY(getY() - 80);
    usernameInput.setText("Username");

    passwordInput = new JFXPasswordField();
    passwordInput.setTranslateX(getX() + 80);
    passwordInput.setTranslateY(getY() - 20);
    passwordInput.setText("password");

    text = new Text(getX() - 20, getY() - 100, "");
    text.setFont(settings.getFont(22));
    root.getChildren().add(passwordInput);
    root.getChildren().add(usernameInput);
    root.getChildren().add(text);
  }


  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    String ret = SQLConnect.getUserdata(usernameInput.getText(), passwordInput.getText());
    if(ret.startsWith("fail")) {
      text.setText("User/password not found");
      text.setFill(Color.RED);
    } else {
      settings.setData(AccountData.fromString(ret));
      text.setText("Logged In");
      text.setFill(Color.GREEN);
      usernameInput.clear();
      passwordInput.clear();
    }
  }

  @Override
  public void removeRender() {
    super.removeRender();
    root.getChildren().remove(usernameInput);
    root.getChildren().remove(passwordInput);
    root.getChildren().remove(text);
  }
}
