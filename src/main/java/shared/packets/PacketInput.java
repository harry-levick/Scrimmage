package shared.packets;

import java.util.UUID;

/**
 * Packet that contains the data on the player input to allow the server to process client control
 */
public class PacketInput extends Packet {

  private boolean leftKey, rightKey, jumpKey, click;
  private double x, y;
  private UUID uuid;

  private int inputSequenceNumber;

  /**
   * Constructs a packet that holds all the data regarding the player
   * @param x X Position of the player
   * @param y Y Position of the player
   * @param leftKey The player's leftKey boolean
   * @param rightKey The player's rightKey boolean
   * @param jumpKey The player's jumpKey boolean
   * @param click If the player has clicked or not in the last update
   * @param uuid Player UUID
   */
  public PacketInput(
      double x,
      double y,
      boolean leftKey,
      boolean rightKey,
      boolean jumpKey,
      boolean click,
      UUID uuid,
      int inputCount) {
    packetID = PacketID.INPUT.getID();
    this.uuid = uuid;
    this.click = click;
    this.leftKey = leftKey;
    this.rightKey = rightKey;
    this.jumpKey = jumpKey;
    this.x = x;
    this.y = y;
    this.inputSequenceNumber = inputSequenceNumber;

    data =
        packetID
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
            + click
            + ","
            + inputCount;
  }

  /**
   * Constructs a packet from a string of data
   * @param data Packet data received from sender
   */
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
    this.inputSequenceNumber = Integer.parseInt(unpackedData[8]);
    this.data =
        packetID
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
            + click
            + ","
            + inputSequenceNumber;
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


  public int getInputSequenceNumber() {
    return inputSequenceNumber;
  }

  public void setInputSequenceNumber(int inputSequenceNumber) {
    this.inputSequenceNumber = inputSequenceNumber;
  }
}
