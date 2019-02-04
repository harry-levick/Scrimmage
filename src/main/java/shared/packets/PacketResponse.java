package shared.packets;

import shared.util.byteFunctions.ByteUtil;

import java.util.UUID;

public class PacketResponse extends Packet {

  private boolean accepted;

  public PacketResponse(boolean accepted, int numberOfPlayers) {
    packetID = 1;
    data =
        ByteUtil.combinedBytes(
            Integer.toString(packetID).getBytes(), Integer.toString(numberOfPlayers).getBytes());
  }

  public PacketResponse(byte[] data) {
    this.data = data;
    String[] unpackedData = data.toString().trim().split(",");
    this.accepted = Boolean.parseBoolean(unpackedData[0]);
  }

  public void addPlayer(double x, double y, UUID uuid) {
    data =
        ByteUtil.combinedBytes(
            data,
            Double.toString(x).getBytes(),
            Double.toString(y).getBytes(),
            ByteUtil.getBytesUUID(uuid));
  }

  public boolean isAccepted() {
    return accepted;
  }
}
