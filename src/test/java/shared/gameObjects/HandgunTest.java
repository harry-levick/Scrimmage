package shared.gameObjects;

import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.gameObjects.weapons.Handgun;
import shared.gameObjects.weapons.Weapon;

public class HandgunTest {

  private static Weapon handgun;

  @BeforeClass
  public static void initHandgun() {
    handgun = new Handgun(10, 10, "HandGun@HandgunTest", null, UUID.randomUUID());
  }

  @Test
  public void test1() {

    assertTrue(true);
  }
}
