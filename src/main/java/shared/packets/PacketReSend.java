package shared.packets;

import java.util.UUID;

/**
 * Packet that alerts the server a game object has been deleted
 */
public class PacketReSend extends Packet {

  private UUID gameObject;


  public PacketReSend(UUID gameObject) {
    packetID = PacketID.RESEND.getID();
    this.gameObject = gameObject;
    data = packetID + "," + gameObject;
  }

  /**
   * Constructs a packet from a string of data
   *
   * @param data Packet data received from sender
   */
  public PacketReSend(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.gameObject = UUID.fromString(unpackedData[1]);
  }

  public UUID getGameobject() {
    return gameObject;
  }
}
