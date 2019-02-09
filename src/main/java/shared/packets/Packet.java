package shared.packets;

public abstract class Packet {

  protected int packetID;
  protected byte[] data;

  public Packet() {}

  public byte[] getData() {
    return data;
  }

  public int getPacketID() {
    return packetID;
  }
}
