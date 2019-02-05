package shared.packets;

import java.util.UUID;

public class PacketPlayerJoin extends Packet {

  private double x;
  private double y;
  private UUID uuid;

  public PacketPlayerJoin(double x, double y, UUID uuid) {
    packetID = PacketID.PLAYERJOIN.getID();
    this.x = x;
    this.y = y;
    this.uuid = uuid;
    data = (packetID + x + y + uuid.toString()).getBytes();
  }

  public PacketPlayerJoin(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.x = Double.parseDouble(unpackedData[1]);
    this.y = Double.parseDouble(unpackedData[2]);
    this.uuid = UUID.fromString(unpackedData[3]);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public UUID getUUID() {
    return uuid;
  }
}
