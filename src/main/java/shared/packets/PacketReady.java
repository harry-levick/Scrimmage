package shared.packets;

import java.util.UUID;

/**
 * Packet sent by client to tell the server they have "readied up" in lobby
 */
public class PacketReady extends Packet {

  private UUID uuid;

  /**
   * Constructs a packet with the UUID of the player who has decided to ready up
   *
   * @param uuid Player UUID
   * @param username Player username
   */
  public PacketReady(UUID uuid, String username) {
    packetID = PacketID.READY.getID();
    this.uuid = uuid;
    data = packetID + "," + uuid;
  }

  /**
   * Constructs a packet from a string of data
   *
   * @param data Packet data received from sender
   */
  public PacketReady(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.uuid = UUID.fromString(unpackedData[1]);
  }

  public UUID getUUID() {
    return uuid;
  }
}
