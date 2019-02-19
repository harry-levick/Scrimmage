package shared.packets;

import java.util.UUID;

public class PacketInput extends Packet {

  private boolean leftKey, rightKey, jumpKey, click;
  private double x, y;
  private UUID uuid;

  public PacketInput(
      double x, double y, boolean leftKey, boolean rightKey, boolean jumpKey, boolean click,
      UUID uuid) {
    packetID = PacketID.INPUT.getID();
    this.uuid = uuid;
    this.click = click;
    this.leftKey = leftKey;
    this.rightKey = rightKey;
    this.jumpKey = jumpKey;
    this.x = x;
    this.y = y;

    data = packetID
            + ","
            + uuid
            + ","
            + x
            + ","
            + y
            + ","
            + leftKey
            + ","
            + rightKey
            + ","
            + jumpKey
            + ","
        + click;
  }

  public PacketInput(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.uuid = UUID.fromString(unpackedData[1]);
    this.x = Double.parseDouble(unpackedData[2]);
    this.y = Double.parseDouble(unpackedData[3]);
    this.leftKey = Boolean.parseBoolean(unpackedData[4]);
    this.rightKey = Boolean.parseBoolean(unpackedData[5]);
    this.jumpKey = Boolean.parseBoolean(unpackedData[6]);
    this.click = Boolean.parseBoolean(unpackedData[7]);
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

  public UUID getUuid() {
    return uuid;
  }
}
