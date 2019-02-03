package shared.gameObjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.weapons.Handgun;
import shared.gameObjects.weapons.Weapon;

public class HandgunTest {

  private static Weapon handgun;

  @BeforeClass
  public static void initHandgun() {
    handgun = new Handgun(
        10,
        10,
        ObjectID.Weapon,
        10,
        30,
        "HandGun",
        40,
        80,
        3,
        1,
        UUID.randomUUID()
    );
  }

  @Test
  public void test1() {
    System.out.println(handgun.toString());
    assertTrue(true);
  }

  @Test
  public void test2() {
    assertEquals(10, handgun.getDamage(), 0.0001f);
  }
}
