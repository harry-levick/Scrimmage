import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import shared.weapons.Handgun;

public class HandgunTest {

  private static Handgun handgun;

  @BeforeClass
  public static void initHandgun() {
    handgun = new Handgun(8f, 3.4f, "Revolver", 7f, 1000f, 120f, 2f);
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
