package client.handlers.userData;

import client.main.Client;

public class ClientData {

  private String username;
  private boolean[] achievements;
  private boolean[] skins;
  private int lootboxCount;
  private int moneyCount;

  public ClientData(String username, boolean[] achievements, boolean[] skins, int lootboxCount, int moneyCount) {
    this.username = username;
    this.achievements = achievements;
    this.skins = skins;
    this.lootboxCount = lootboxCount;
    this.moneyCount = moneyCount;
  }

  public static ClientData fromString(String data) {
    String[] splitData = data.split("//x/s");
    boolean[] achievements = new boolean[30];
    int packedAchievements = Integer.parseInt(splitData[1]);
    for (int i = 29; i >= 0; i--)
      achievements[29 - i] = (packedAchievements & (1 << i)) != 0;
    boolean[] skins = new boolean[30];
    int packedSkins = Integer.parseInt(splitData[2]);
    for (int i = 29; i >= 0; i--)
      achievements[29 - i] = (packedAchievements & (1 << i)) != 0;
    return new ClientData(splitData[0], achievements, skins, Integer.parseInt(splitData[3]), Integer.parseInt(splitData[4]));
  }

  public String[] saveQuery() {
    int packedAchievements = 0;
    for (int i = 0; i < achievements.length; i++)
      packedAchievements = (packedAchievements << 1) | (achievements[i] ? 1 : 0);
    int packedSkins = 0;
    for (int i = 0; i < skins.length; i++)
      packedSkins = (packedSkins << 1) | (skins[i] ? 1 : 0);
    String[] ret = {username, Integer.toString(packedAchievements), Integer.toString(packedSkins), Integer.toString(lootboxCount), Integer.toString(moneyCount)};
    return ret;
  }

  //TODO queue change to sql
  public boolean[] getAchievements() {
    return achievements;
  }

  public void getAchievement(int id) {
    achievements[id] = true;
  }

  public boolean[] getSkins() {
    return skins;
  }

  public void getSkin(int id) {
   skins[id] = true;
  }

  public int getLootboxCount() {
    return lootboxCount;
  }

  public void openLootbox() {
    lootboxCount++;
  }

  public void getLootbox() {
    lootboxCount--;
  }

  public int getMoneyCount() {
    return moneyCount;
  }

  public void addMoney(int value) {
    moneyCount += value;
  }

  public void removeMoney(int value) {
    moneyCount -= value;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
