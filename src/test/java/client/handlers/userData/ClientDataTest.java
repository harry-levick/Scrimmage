package client.handlers.userData;

import static org.junit.Assert.*;

import client.main.Client;
import org.junit.Test;

public class ClientDataTest {

  @Test
  public void fromString() {
    boolean[] achievements = new boolean[30];
    achievements[2] = true;
    achievements[5] = true;
    ClientData source = new ClientData("SamuraiJack2993", achievements, achievements, 12, 369);
    String[] saveQuery = source.saveQuery();
    String out = saveQuery[0] + "//x/s" + saveQuery[1] + "//x/s" + saveQuery[2] + "//x/s" + saveQuery[3] + "//x/s" + saveQuery[4];
    ClientData destination = ClientData.fromString(out);
    assertEquals(destination.getUsername(), source.getUsername());
    assertEquals(destination.getAchievements()[2], source.getAchievements()[2]);
    assertEquals(destination.getAchievements()[5], source.getAchievements()[5]);
    assertEquals(destination.getLootboxCount(), source.getLootboxCount());
    assertEquals(destination.getMoneyCount(), source.getMoneyCount());
  }
}