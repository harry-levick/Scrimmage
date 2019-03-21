package shared.handlers.levelHandler;

/**
 * @author Brett Saunders
 * Enum to determine the current state of the application
 */
public enum GameState {
  /**
   * Main Menu
   */
  MAIN_MENU,
  /**
   * Settings page
   */
  SETTINGS,
  /**
   * In Active Game
   */
  IN_GAME,
  /**
   * Multiplayer Lobby waiting for game to start
   */
  LOBBY,
  /**
   * Config page to host game
   */
  HOST,
  /**
   * Config page to join game
   */
  START_CONNECTION,
  /**
   * Multiplayer menu to select Host/Join
   */
  MULTIPLAYER
}
