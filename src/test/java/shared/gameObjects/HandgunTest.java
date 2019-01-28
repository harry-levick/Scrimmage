package shared.gameObjects;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.weapons.Weapon;
import shared.gameObjects.weapons.Handgun;

public class HandgunTest {

  private static Weapon handgun;

  @BeforeClass
  public static void initHandgun() {
    handgun = new Handgun(
        10, 
        10,
        ObjectID.Player,
        10,
        10,
        "HandGun",
        30,
        40,
        80,
        3
    );
  }

  @Test
  public void test1() {
    System.out.println(handgun.toString());
    assertTrue(true);
  }

  @Test
  public void test2() {
    assertEquals(8f, handgun.getDamage(), 0.0001f);
  }
}
