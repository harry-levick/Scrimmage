package shared.packets;

/**
 * Packet that alerts the server a game object has been deleted
 */
public class PacketMap extends Packet {

  private String map;


  public PacketMap(String map, boolean i) {
    packetID = PacketID.MAP.getID();
    this.map = map;
    data = packetID + "," + map;
  }

  /**
   * Constructs a packet from a string of data
   *
   * @param data Packet data received from sender
   */
  public PacketMap(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.map = unpackedData[1];
  }

  public String getMap() {
    return map;
  }
}
