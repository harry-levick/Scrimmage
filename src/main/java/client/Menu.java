package client;

/**
 * Enum container for the menu-based maps
 */
public enum Menu {
  MAINMENU("menus/main_menu.map"),
  LOBBY("menus/lobby.map"),
  MULTIPLAYER("menus/multiplayer.map"),
  HOST("menus/host.map"),
  JOIN("menus/join.map"),
  SETTINGS("menus/settings.map");

  private String menuPath;

  Menu(String menuPath) {
    this.menuPath = menuPath;
  }

  public String getMenuPath() {
    return this.menuPath;
  }
}
