package shared.packets;

import java.util.UUID;

public class PacketJoin extends Packet {

  private UUID clientID;
  private String username;
  // private int skin;

  public PacketJoin(UUID clientID, String username) {
    packetID = PacketID.JOIN.getID();
    this.clientID = clientID;
    this.username = username;
    data = (Integer.toString(packetID) + "," + clientID.toString() + "," + username).getBytes();
  }

  public PacketJoin(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.clientID = UUID.fromString(unpackedData[1]);
    this.username = unpackedData[2];
  }

  public UUID getClientID() {
    return clientID;
  }

  public String getUsername() {
    return username;
  }
}
