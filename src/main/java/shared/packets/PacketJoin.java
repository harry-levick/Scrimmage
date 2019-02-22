package shared.packets;

import java.util.UUID;

public class PacketJoin extends Packet {

  private UUID clientID;
  private String username;
  private double x, y;
  // private int skin;

  public PacketJoin(UUID clientID, String username, double x, double y) {
    packetID = PacketID.JOIN.getID();
    this.clientID = clientID;
    this.username = username;
    this.x = x;
    this.y = y;
    data = packetID + "," + clientID.toString() + "," + username + "," + x + "," + y;
  }

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
