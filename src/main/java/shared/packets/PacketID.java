package shared.packets;

/**
 * PacketID enum and ints to allow client and server to send messages; the IDs are processed to know which packet is being sent/received
 */
public enum PacketID {
  /**
   * PacketJoin ID
   */
  JOIN(0),
  /**
   * PacketResponse ID
   */
  RESPONSE(1),
  /**
   * PacketInput ID
   */
  INPUT(2),
  /**
   * PacketMap ID
   */
  MAP(3),
  /**
   * PlayerJoin ID
   */
  PLAYERJOIN(4),
  /**
   * PacketReady ID
   */
  READY(5),
  /**
   * PacketEnd ID
   */
  END(6),
  /**
   * PacketGameState ID
   */
  GAMESTATE(7),
  /**
   * Gameobject deleted
   */
  DELETE(8);

  private int packetID;

  PacketID(int packetID) {
    this.packetID = packetID;
  }

  /**
   * Gets the int value of the ID to be able to send it via the network
   * @return the int value of the packet ID
   */
  public int getID() {
    return this.packetID;
  }
}
