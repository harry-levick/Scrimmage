package shared.packets;


import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class PacketInputTest {

  private double x = 10;
  private double y = 213.12;
  private boolean leftKey = false;
  private boolean rightKey = true;
  private boolean jumpKey = true;
  private boolean click = false;

  @Test
  public void sameInSameOut() {
    PacketInput input = new PacketInput(x, y, leftKey, rightKey, jumpKey, click);
    byte[] simulateNetwork = input.getData();
    PacketInput output = new PacketInput(simulateNetwork);
    assertEquals(output.getX(), x);
    assertEquals(output.getY(), y);
    assertEquals(output.isLeftKey(), leftKey);
    assertEquals(output.isRightKey(), rightKey);
    assertEquals(output.isJumpKey(), jumpKey);
    assertEquals(output.isClick(), click);
  }

}