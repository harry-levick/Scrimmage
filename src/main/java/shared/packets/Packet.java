package shared.packets;

public abstract class Packet {

  protected int packetID;
  protected String data;

  public Packet() {}

  public byte[] getData() {
    return data.getBytes();
  }

  public String getString() {
    return data;
  }

  public int getPacketID() {
    return packetID;
  }
}
