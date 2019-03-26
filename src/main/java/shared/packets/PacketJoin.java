package shared.packets;

import java.util.UUID;

/**
 * Packet that alerts the server a player has joined
 */
public class PacketJoin extends Packet {

  private UUID clientID;
  private String username;
  private double x, y;
  // private int skin;

  /**
   * Construct a packet that contains the data needed by the server to create and process a client
   * @param clientID Player UUID
   * @param username The username the player has set for themselves
   * @param x X Position of the player
   * @param y Y Position of the player
   */
  public PacketJoin(UUID clientID, String username, double x, double y) {
    packetID = PacketID.JOIN.getID();
    this.clientID = clientID;
    this.username = username;
    this.x = x;
    this.y = y;
    data = packetID + "," + clientID.toString() + "," + username + "," + x + "," + y;
  }

  /**
   * Constructs a packet from a string of data
   * @param data Packet data received from sender
   */
  public PacketJoin(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.clientID = UUID.fromString(unpackedData[1]);
    this.username = unpackedData[2];
    this.x = Double.parseDouble(unpackedData[3]);
    this.y = Double.parseDouble(unpackedData[4]);
  }

  public UUID getClientID() {
    return clientID;
  }

  public String getUsername() {
    return username;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
