package shared.packets;

/**
 * Packet representing the end of the game
 */
public class PacketEnd extends Packet {

  /**
   * Constructs the end of game packet; only an ID is needed to process this
   */
  public PacketEnd() {
    packetID = PacketID.END.getID();
    data = Integer.toString(packetID);
  }

  /**
   * Constructs a packet from a string of data
   *
   * @param data Packet data received from sender
   */
  public PacketEnd(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
  }
}
