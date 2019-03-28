package client.handlers.accountHandler;

import java.util.Random;

public class Lootbox {

  private static final double SKIN_CHANCE = 0.333;
  private static final double LARGE_MONEY_CHANCE = 0.35;
  private static final int LARGE_MONEY = 350;
  private static final int SMALL_MONEY = 100;

  public static String rollLootbox(AccountData data) {
    String toDisplay = "";
    data.openLootbox();
    Random random = new Random();
    if(random.nextDouble() <= SKIN_CHANCE) {
      data.awardSkin(random.nextInt(data.SKIN_COUNT - 1) + 1) ;
      toDisplay = "Earned Skin!";
    } else {
      if(random.nextDouble() <= LARGE_MONEY_CHANCE) {
        data.addMoney(LARGE_MONEY);
        toDisplay = "Earned " + LARGE_MONEY + " Scrimbucks!";
      } else {
        data.addMoney(SMALL_MONEY);
        toDisplay = "Earned " + SMALL_MONEY + " Scrimbucks!";
      }
    }
    return toDisplay;
  }
}
