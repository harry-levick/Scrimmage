package shared.packets;


public class PacketInput extends Packet {

  private boolean leftKey, rightKey, jumpKey, click;
  private double x, y;

  public PacketInput(double x, double y, boolean leftKey, boolean rightKey, boolean jumpKey,
      boolean click) {
    packetID = PacketID.INPUT.getID();
    this.click = click;
    this.leftKey = leftKey;
    this.rightKey = rightKey;
    this.jumpKey = jumpKey;
    this.x = x;
    this.y = y;

    data = (Integer.toString(packetID) + "," + Double.toString(x) + "," +
        Double.toString(y) + "," + Boolean.toString(leftKey) + "," +
        Boolean.toString(rightKey) + "," + Boolean.toString(jumpKey) + "," +
        Boolean.toString(click)).getBytes();
  }

  public PacketInput(byte[] data) {
    String temp = new String(data).trim();
    String[] unpackedData = temp.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.x = Double.parseDouble(unpackedData[1]);
    this.y = Double.parseDouble(unpackedData[2]);
    this.leftKey = Boolean.parseBoolean(unpackedData[3]);
    this.rightKey = Boolean.parseBoolean(unpackedData[4]);
    this.jumpKey = Boolean.parseBoolean(unpackedData[5]);
    this.click = Boolean.parseBoolean(unpackedData[6]);
  }

  public boolean isLeftKey() {
    return leftKey;
  }

  public boolean isRightKey() {
    return rightKey;
  }

  public boolean isJumpKey() {
    return jumpKey;
  }

  public boolean isClick() {
    return click;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
