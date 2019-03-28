package shared.util.maths;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class Vector2Test {

  static Vector2 unit;
  static Vector2 twoThree;
  static Vector2 fourSeven;
  static float scalarTwo, scalarNine;

  @Before
  public void setUp() throws Exception {
    unit = Vector2.Unit();
    twoThree = new Vector2(2, 3);
    fourSeven = new Vector2(4, 7);
    scalarTwo = 2;
    scalarNine = 9;
  }

  @Test
  public void max() {
    assertTrue(Vector2.max(twoThree, fourSeven).equals(fourSeven));
  }

  @Test
  public void min() {
    assertTrue(Vector2.min(twoThree, fourSeven).equals(twoThree));
  }

  @Test
  public void mult() {
    assertTrue(
        twoThree
            .mult(scalarTwo)
            .equals(new Vector2(twoThree.getX() * scalarTwo, twoThree.getY() * scalarTwo)));
  }

  @Test
  public void mult1() {
    assertTrue(
        twoThree
            .mult(fourSeven)
            .equals(
                new Vector2(
                    twoThree.getX() * fourSeven.getX(), twoThree.getY() * fourSeven.getY())));
  }

  @Test
  public void add() {
    assertTrue(
        twoThree
            .add(scalarTwo)
            .equals(new Vector2(twoThree.getX() + scalarTwo, twoThree.getY() + scalarTwo)));
  }

  @Test
  public void add1() {
    assertTrue(
        twoThree
            .add(fourSeven)
            .equals(
                new Vector2(
                    twoThree.getX() + fourSeven.getX(), twoThree.getY() + fourSeven.getY())));
  }

  @Test
  public void sub() {
    assertTrue(
        twoThree
            .sub(scalarTwo)
            .equals(new Vector2(twoThree.getX() - scalarTwo, twoThree.getY() - scalarTwo)));
  }

  @Test
  public void sub1() {
    assertTrue(
        twoThree
            .sub(fourSeven)
            .equals(
                new Vector2(
                    twoThree.getX() - fourSeven.getX(), twoThree.getY() - fourSeven.getY())));
  }

  @Test
  public void div() {
    assertTrue(
        twoThree
            .div(scalarTwo)
            .equals(new Vector2(twoThree.getX() / scalarTwo, twoThree.getY() / scalarTwo)));
  }

  @Test
  public void div1() {
    assertTrue(
        twoThree
            .div(fourSeven)
            .equals(
                new Vector2(
                    twoThree.getX() / fourSeven.getX(), twoThree.getY() / fourSeven.getY())));
  }

  @Test
  public void dot() {
    assertEquals(29.0, twoThree.dot(fourSeven), 0.000000000001);
  }

  @Test
  public void magnitude() {
    assertEquals(4.69, twoThree.magnitude(fourSeven), 0.04 * 4.69);
  }

  @Test
  public void magnitude1() {
    assertEquals(3.6, twoThree.magnitude(), 0.04 * 3.6);
  }

  @Test
  public void exactMagnitude() {
    assertEquals(3.6055512, twoThree.exactMagnitude(Vector2.Zero()), 0.0000001);
  }

  @Test
  public void angleBetween() {
    assertEquals(0.326, twoThree.angleBetween(fourSeven), 0.001);
  }

  @Test
  public void angle() {
    assertEquals(0.982, twoThree.angle(), 0.001);
  }

  @Test
  public void cross() {
    assertTrue(
        twoThree
            .cross(scalarNine)
            .equals(new Vector2(twoThree.getX() * scalarNine, twoThree.getY() * -scalarNine)));
  }

  @Test
  public void cross1() {
    assertTrue(twoThree.cross(fourSeven).equals(
        new Vector2(twoThree.getX() * fourSeven.getY(), twoThree.getY() * -fourSeven.getX())));
  }

  @Test
  public void clamp() {
    assertTrue(twoThree.clamp(Vector2.Zero(), Vector2.Unit()).equals(Vector2.Unit()));
  }

  @Test
  public void normalize() {
    assertTrue(twoThree.normalize().equals(new Vector2(twoThree.getX() / twoThree.magnitude(),
        twoThree.getY() / twoThree.magnitude())));
  }

  @Test
  public void normal() {
    assertTrue(twoThree.normal().equals(new Vector2(-twoThree.getY(), twoThree.getX())));
  }

  @Test
  public void canOverlap() {
    assertTrue(!(twoThree.canOverlap(fourSeven)));
  }


  @Test
  public void equals() {
    assertTrue(Vector2.Unit().equals(Vector2.Unit()));
  }
}
