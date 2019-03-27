package shared.packets;

import java.util.UUID;

public class PacketJoin extends Packet {

  private double x;
  private double y;
  private UUID uuid;
  private String username;
  private UUID legLeftUUID, legRightUUID, bodyUUID, headUUID, armLeftUUID, armRightUUID, handLeftUUID, handRightUUID;

  /**
   * Construct a packet that contains the data needed by the server to create and process a client
   * @param username The username the player has set for themselves
   * @param x X Position of the player
   * @param y Y Position of the player
   * @param uuid Players uuid
   * @param legLeftUUID Left Leg uuid
   * @param legRightUUID Right leg uuid
   * @param bodyUUID Body uuid
   * @param headUUID Head uuid
   * @param armLeftUUID Left arm uuid
   * @param armRightUUID Right arm uuid
   * @param handLeftUUID Left hand uuid
   * @param handRightUUID Right hand uuid
   */
  public PacketJoin(UUID uuid, String username, double x, double y, UUID legLeftUUID,
      UUID legRightUUID, UUID bodyUUID, UUID headUUID, UUID armLeftUUID, UUID armRightUUID,
      UUID handLeftUUID, UUID handRightUUID) {
    packetID = PacketID.PLAYERJOIN.getID();
    this.x = x;
    this.y = y;
    this.uuid = uuid;
    this.username = username;
    this.legLeftUUID = legLeftUUID;
    this.legRightUUID = legRightUUID;
    this.bodyUUID = bodyUUID;
    this.armLeftUUID = armLeftUUID;
    this.armRightUUID = armRightUUID;
    this.headUUID = headUUID;
    this.handLeftUUID = handLeftUUID;
    this.handRightUUID = handRightUUID;
    data = packetID + "," + x + "," + y + "," + uuid + "," + username + "," + legLeftUUID + ","
        + legRightUUID + "," + bodyUUID + "," + armLeftUUID + "," + armRightUUID + "," + headUUID
        + "," + handLeftUUID + "," + handRightUUID;
  }
  /**
   * Constructs a packet from a string of data
   * @param data Packet data received from sender
   */
  public PacketJoin(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.x = Double.parseDouble(unpackedData[1]);
    this.y = Double.parseDouble(unpackedData[2]);
    this.uuid = UUID.fromString(unpackedData[3]);
    this.username = unpackedData[4];
    this.legLeftUUID = UUID.fromString(unpackedData[5]);
    this.legRightUUID = UUID.fromString(unpackedData[6]);
    this.bodyUUID = UUID.fromString(unpackedData[7]);
    this.armLeftUUID = UUID.fromString(unpackedData[8]);
    this.armRightUUID = UUID.fromString(unpackedData[9]);
    this.headUUID = UUID.fromString(unpackedData[10]);
    this.handLeftUUID = UUID.fromString(unpackedData[11]);
    this.handRightUUID = UUID.fromString(unpackedData[12]);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public UUID getClientID() {
    return uuid;
  }

  public String getUsername() {
    return username;
  }

  public UUID getLegLeftUUID() {
    return legLeftUUID;
  }

  public UUID getLegRightUUID() {
    return legRightUUID;
  }

  public UUID getBodyUUID() {
    return bodyUUID;
  }

  public UUID getHeadUUID() {
    return headUUID;
  }

  public UUID getArmLeftUUID() {
    return armLeftUUID;
  }

  public UUID getArmRightUUID() {
    return armRightUUID;
  }

  public UUID getHandLeftUUID() {
    return handLeftUUID;
  }

  public UUID getHandRightUUID() {
    return handRightUUID;
  }
}
