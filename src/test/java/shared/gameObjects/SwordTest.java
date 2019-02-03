package shared.gameObjects;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.weapons.Weapon;
import shared.gameObjects.weapons.Melee;
import shared.gameObjects.weapons.Sword;

public class SwordTest {
  
  private static Melee sword;
  private static double delta = 0.001;
  
  @BeforeClass
 // public static void initSword() {
  //  sword = new Sword(10, 10, 10, 10, "Sword", 30, 5, 70, 20);
//  }
  
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
    assertTrue(sword.getId() == ObjectID.Player);
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
    assertTrue(sword.getName() == "Sword");
  }
  
  @Test
  public void testRange() {
    assertEquals(5, ((Melee) sword).getRange(), delta);
  }
  
  @Test
  public void testBeginAngle() {
    assertEquals(70, ((Melee) sword).getBeginAngle(), delta);
  }
  
  @Test
  public void testEndAngle() {
    assertEquals(20, ((Melee) sword).getEndAngle(), delta);
  }
  
}
