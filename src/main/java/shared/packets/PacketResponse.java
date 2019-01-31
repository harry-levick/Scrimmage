package shared.packets;

import java.util.UUID;
import shared.gameObjects.players.Player;
import shared.util.byteFunctions.ByteUtil;

public class PacketResponse extends Packet {

  private boolean accepted;
  private int numberOfPlayers;

  public PacketResponse(boolean accepted, int numberOfPlayers) {
    packetID = 1;
    data = ByteUtil.combinedBytes(Integer.toString(packetID).getBytes(),
        Integer.toString(numberOfPlayers).getBytes());
  }

  public PacketResponse(byte[] data) {
    this.data = data;
    String[] unpackedData = data.toString().trim().split(",");
    this.accepted = Boolean.parseBoolean(unpackedData[0]);
    this.numberOfPlayers = Integer.parseInt(unpackedData[1]);
  }

  public void addPlayer(double x, double y, UUID uuid) {
    data = ByteUtil
        .combinedBytes(data, Double.toString(x).getBytes(), Double.toString(y).getBytes(),
            ByteUtil.getBytesUUID(uuid));
  }

  public Player createPlayer(int playerNO) {

  }

  public boolean isAccepted() {
    return accepted;
  }

  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }
}
