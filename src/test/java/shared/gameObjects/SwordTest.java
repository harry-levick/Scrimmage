package shared.gameObjects;

import org.junit.BeforeClass;
import org.junit.Test;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.weapons.Melee;
import shared.gameObjects.weapons.Sword;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SwordTest {

  private static Melee sword;
  private static double delta = 0.001;

  @BeforeClass
  public static void initSword() {
    sword =
        new Sword(
            10, 10, 100, 100,10, 10, "test", 100, 5, 70, 20, UUID.randomUUID());
  }

  @Test
  public void testGetX() {
    assertEquals(10, sword.getX(), delta);
  }

  @Test
  public void testGetY() {
    assertEquals(10, sword.getY(), delta);
  }

  @Test
  public void testGetId() {
    assertTrue(sword.getId() == ObjectID.Weapon);
  }

  @Test
  public void testGetDamage() {
    assertEquals(10, sword.getDamage(), delta);
  }

  @Test
  public void testGetWeight() {
    assertEquals(10, sword.getWeight(), delta);
  }

  @Test
  public void testGetName() {
    assertTrue(sword.getName() == "test");
  }

  @Test
  public void testRange() {
    assertEquals(70, ((Melee) sword).getRange(), delta);
  }

  @Test
  public void testBeginAngle() {
    assertEquals(20, ((Melee) sword).getBeginAngle(), delta);
  }

  @Test
  public void testEndAngle() {
    assertEquals(20, ((Melee) sword).getEndAngle(), delta);
  }
}
