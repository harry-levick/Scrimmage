package client.handlers.inputHandler;

import client.main.Client;
import client.main.Settings.KEY_CONTROL;
import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import shared.gameObjects.players.Player;

/**
 * Records Keyboard input for the client
 */
public class KeyboardInput implements EventHandler<KeyEvent> {

  private Player clientPlayer;
  private boolean changeListening = false;
  private KEY_CONTROL hotKeyChange = null;

  /**
   * Default constructor
   */
  public KeyboardInput() {
    this.clientPlayer = Client.levelHandler.getClientPlayer();
  }

  @Override
  public void handle(KeyEvent event) {
    Client.sendUpdate = true;

    KeyCode keyLeft = Client.settings.getKeyMap(KEY_CONTROL.LEFT);
    KeyCode keyRight = Client.settings.getKeyMap(KEY_CONTROL.RIGHT);
    KeyCode keyJump = Client.settings.getKeyMap(KEY_CONTROL.JUMP);
    KeyCode keyThrow = Client.settings.getKeyMap(KEY_CONTROL.THROW);
    KeyCode keyMenu = Client.settings.getKeyMap(KEY_CONTROL.MENU);

    KeyCode k = event.getCode();
    if (changeListening) {

      switch (hotKeyChange) {
        case JUMP:
          Client.settings.setKeyMap(KEY_CONTROL.JUMP, k);
          break;
        case LEFT:
          Client.settings.setKeyMap(KEY_CONTROL.LEFT, k);
          break;
        case RIGHT:
          Client.settings.setKeyMap(KEY_CONTROL.RIGHT, k);
          break;
        case THROW:
          Client.settings.setKeyMap(KEY_CONTROL.THROW, k);
          break;
        case MENU:
          Client.settings.setKeyMap(KEY_CONTROL.MENU, k);
          break;
          default: break;
      }
      changeListening = false;

    } else if (event.getEventType() == KeyEvent.KEY_PRESSED) {


      // switch requires constant at compile time, use if else instead
      if (k == keyLeft) {
        clientPlayer.leftKey = true;
      } else if (k == keyRight) {
        clientPlayer.rightKey = true;
      } else if (k == keyJump) {
        clientPlayer.jumpKey = true;
      } else if (k == keyThrow) {
        clientPlayer.throwHoldingKey = true;
      } else if (k == keyMenu) {
        Client.settingsToggle();
      } else if (k == KeyCode.K) {
        clientPlayer.deattach = true;
      } else {
        Client.sendUpdate = false;
      }

//      switch (event.getCode()) {
//        case keyLeft:
//          clientPlayer.leftKey = true;
//          break;
//        case D:
//          clientPlayer.rightKey = true;
//          break;
//        case W:
//          clientPlayer.jumpKey = true;
//          break;
//        case F:
//          clientPlayer.throwHoldingKey = true;
//          break;
//        case H:
//          clientPlayer.deattach = true;
//          break;
//        case ESCAPE:
//          Client.settingsToggle();
//        default:
//          Client.sendUpdate = false;
    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      if (k == keyLeft) {
        clientPlayer.leftKey = false;
      } else if (k == keyRight) {
        clientPlayer.rightKey = false;
      } else if (k == keyJump) {
        clientPlayer.jumpKey = false;
      } else if (k == keyThrow) {
        clientPlayer.throwHoldingKey = false;
      } else if (k == keyMenu) {
        // no action
      } else if (k == KeyCode.K) {
        clientPlayer.deattach = false;
      } else {
        Client.sendUpdate = false;
      }

    }
  }

  public boolean getChangeListening() {
    return changeListening;
  }

  public void setChangeListening(boolean listen) {
    changeListening = listen;
  }

  public void setChangeListening(KEY_CONTROL key) {
    changeListening = true;
    hotKeyChange = key;
  }

  public KEY_CONTROL getHotChangeKey() {
    return hotKeyChange;
  }
}
