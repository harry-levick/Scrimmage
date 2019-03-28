package client.handlers.accountHandler;

/**
 * Container class for the save data of the Client
 */
public class AccountData {

  private String username;
  private String uuid;
  private boolean[] achievements;
  private boolean[] skins;
  private int activeSkin[];
  private int lootboxCount;
  private int moneyCount;
  public static final int SKIN_COUNT = 9;

  /**
   * Constructs a client data with the given unlocks
   * @param uuid UUID of the user, as can be found on the SQL Database
   * @param username Name of the user, shown to other players and as found on the SQL Database
   * @param achievements Boolean array representing unlock state of achievements
   * @param skins Boolean array representing unlock state of player skins
   * @param lootboxCount The number of unopened lootboxes left to open
   * @param moneyCount The amount of in-game currency currently held by the user
   */
  public AccountData(String uuid, String username, boolean[] achievements, boolean[] skins, int lootboxCount, int moneyCount) {
    this.username = username;
    this.achievements = achievements;
    this.skins = skins;
    this.lootboxCount = lootboxCount;
    this.moneyCount = moneyCount;
    this.uuid = uuid;
    skins[0] = true;
    activeSkin = new int[4];
    activeSkin[0] = 8;
    activeSkin[1] = 8;
    activeSkin[2] = 8;
    activeSkin[3] = 8;
    achievements[2] = true;
  }

  /**
   * Constructs ClientData from a formatted string usually obtained by the database
   * @param data Data representing content of player
   * @return ClientData processed from String data
   */
  public static AccountData fromString(String data) {
    String[] splitData = data.split("//x/s");
    boolean[] achievements = new boolean[30];
    int packedAchievements = Integer.parseInt(splitData[2]);
    for (int i = 29; i >= 0; i--)
      achievements[29 - i] = (packedAchievements & (1 << i)) != 0;
    boolean[] skins = new boolean[30];
    int packedSkins = Integer.parseInt(splitData[3]);
    for (int i = 29; i >= 0; i--)
      achievements[29 - i] = (packedAchievements & (1 << i)) != 0;
    return new AccountData(splitData[0], splitData[1], achievements, skins, Integer.parseInt(splitData[4]), Integer.parseInt(splitData[5]));
  }

  /**
   * Generates the String array used to query the server for saving the state of the user
   * @return String array used by SQL Save Query
   */
  public String[] saveQuery() {
    int packedAchievements = 0;
    for (int i = 0; i < achievements.length; i++)
      packedAchievements = (packedAchievements << 1) | (achievements[i] ? 1 : 0);
    int packedSkins = 0;
    for (int i = 0; i < skins.length; i++)
      packedSkins = (packedSkins << 1) | (skins[i] ? 1 : 0);
    String[] ret = {uuid, username, Integer.toString(packedAchievements), Integer.toString(packedSkins), Integer.toString(lootboxCount), Integer.toString(moneyCount)};
    return ret;
  }

  /**
   * Generates the String array used to query the server for registering a new user
   * @return String array used by SQL Register Query
   */
  public String[] registerAccountQuery(String password) {
    String[] save = saveQuery();
    String[] ret = {uuid, username, password, save[2], save[3], save[4], save[5]};
    return ret;
  }


  public boolean[] getAchievements() {
    return achievements;
  }

  /**
   * Sets the achievement with the passed ID to true
   * @param id Achievement ID to award to user
   */
  public void awardAchievement(int id) {
    achievements[id] = true;
    SQLConnect.saveData(this);
  }

  public boolean[] getSkins() {
    return skins;
  }

  /**
   * Allows the user to now use the skin of the given ID
   * @param id Skin ID to award to user
   */
  public void awardSkin(int id) {
   skins[id] = true;
    SQLConnect.saveData(this);
  }

  public boolean hasSkin(int id) {
    try {
      return skins[id];
    } catch (NullPointerException e) {
      return false;
    }
  }

  public int getLootboxCount() {
    return lootboxCount;
  }

  public void earnLootbox() {
    lootboxCount++;
    SQLConnect.saveData(this);
  }

  public void openLootbox() {
    lootboxCount--;
    SQLConnect.saveData(this);
  }

  public int getMoneyCount() {
    return moneyCount;
  }

  public void addMoney(int value) {
    moneyCount += value;
    SQLConnect.saveData(this);
  }

  public void removeMoney(int value) {
    moneyCount -= value;
    SQLConnect.saveData(this);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void applySkin(int head, int body, int legs, int arms) {
    activeSkin[0] = head;
    activeSkin[1] = body;
    activeSkin[2] = legs;
    activeSkin[3] = arms;
  }

  public void applySkin(int[] newSkin) {
    activeSkin = newSkin;
  }
  public int[] getActiveSkin() {
    return activeSkin;
  }

  public String getUUID() {
    return uuid;
  }

  public int getSkinCount() {
    int ret = 0;
    for (boolean skin : skins) {
        if(skin) ret++;
    }
    return ret;
  }

  public int getAchievementCount() {
    int ret = 0;
    for (boolean skin : achievements) {
      if(skin) ret++;
    }
    return ret;
  }
}
