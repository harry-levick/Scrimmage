package shared.gameObjects.weapons;

import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.BeforeClass;
import org.junit.Test;

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
