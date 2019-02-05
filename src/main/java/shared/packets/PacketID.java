package shared.packets;

public enum PacketID {
  JOIN(0),
  RESPONSE(1),
  INPUT(2);

  private int packetID;

  private PacketID(int packetID) {
    this.packetID = packetID;
  }

  public int getID() {
    return this.packetID;
  }
}
