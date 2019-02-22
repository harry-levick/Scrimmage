package shared.packets;

import java.util.UUID;

public class PacketReady extends Packet {

  private UUID uuid;

  public PacketReady(double x, double y, UUID uuid, String username) {
    packetID = PacketID.PLAYERJOIN.getID();
    this.uuid = uuid;
    data = packetID + "," + uuid;
  }

  public PacketReady(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.uuid = UUID.fromString(unpackedData[1]);
  }

  public UUID getUUID() {
    return uuid;
  }
}
