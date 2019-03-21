package shared.packets;

import java.util.UUID;

public class PacketMap extends Packet {

  private UUID uuid;
  private String name;

  //TODO: JavaDoc this boi
  public PacketMap(String name, UUID uuid) {
    packetID = PacketID.MAP.getID();
    this.name = name;
    this.uuid = uuid;
    data = packetID + "," + name;
  }
  /**
   * Constructs a packet from a string of data
   * @param data Packet data received from sender
   */
  public PacketMap(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.name = unpackedData[1];
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }
}
