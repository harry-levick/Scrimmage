package shared.packets;

import java.util.UUID;
import shared.util.byteFunctions.ByteUtil;

public class PacketJoin extends Packet {

  private UUID clientID;
  private String username;
  //private int skin;

  public PacketJoin(UUID clientID, String username) {
    packetID = PacketID.JOIN.getID();
    this.clientID = clientID;
    this.username = username;
    data = ByteUtil
        .combinedBytes(Integer.toString(packetID).getBytes(), ByteUtil.getBytesUUID(clientID),
            username.getBytes());
  }

  public PacketJoin(byte[] data) {
    String[] unpackedData = data.toString().trim().split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.clientID = ByteUtil.getUUID(unpackedData[1].getBytes());
    this.username = unpackedData[2];
  }


  public UUID getClientID() {
    return clientID;
  }

  public String getUsername() {
    return username;
  }
}
