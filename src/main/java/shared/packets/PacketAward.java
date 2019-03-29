package shared.packets;

import java.util.UUID;
import shared.gameObjects.players.Player;

/**
 * Packet to handle awards such as Achievements being sent
 */
public class PacketAward extends Packet{
  private UUID playerUUID;
  private int awardValue;
  private int awardID;

  public PacketAward(AwardID id, int value, Player player) {
    this.awardID = id.getID();
    this.awardValue = value;
    this.playerUUID = player.getUUID();
    data = packetID + "," + awardID + "," + awardValue + "," + playerUUID;
  }

  public PacketAward(String data) {
    String[] unpackedData = data.split(",");
    this.awardID =  Integer.parseInt(unpackedData[1]);
    this.awardValue =  Integer.parseInt(unpackedData[2]);
    this.playerUUID = UUID.fromString(unpackedData[3]);
  }

  public int getAwardID() {
    return awardID;
  }

  public int getAwardValue() {
    return awardValue;
  }

  public UUID getPlayerUUID() {
    return playerUUID;
  }

  public enum AwardID {
    ACHIEVEMENT(0),
    MONEY(1);

    private int awardID;

    AwardID(int packetID) {
      this.awardID = packetID;
    }

    /**
     * Gets the int value of the ID to be able to send it via the network
     *
     * @return the int value of the packet ID
     */
    public int getID() {
      return this.awardID;
    }
  }

}
