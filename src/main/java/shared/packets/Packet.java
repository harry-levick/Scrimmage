package shared.packets;

/**
 * Base class for network packets
 */
public abstract class Packet {

  /**
   * ID of packet (Determined by PacketID enum) used to let the client/server know what type of
   * packet is being sent.
   */
  protected int packetID;
  /**
   * Data contained in the packet to send
   */
  protected String data;

  /**
   * Base constructor (must be overriden to do anything)
   */
  public Packet() {
  }

  /**
   * Receive the string of data in byte form
   *
   * @return Byte array of data string
   */
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
