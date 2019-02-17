package shared.packets;

public enum PacketID {
  JOIN(0),
  RESPONSE(1),
  INPUT(2),
  MAP(3),
  PLAYERJOIN(4),
  READY(5),
  END(6),
  GAMESTATE(7);

  private int packetID;

  PacketID(int packetID) {
    this.packetID = packetID;
  }

  public int getID() {
    return this.packetID;
  }
}
