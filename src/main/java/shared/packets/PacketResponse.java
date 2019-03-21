package shared.packets;

public class PacketResponse extends Packet {

  private boolean accepted;
  private String multiAddress;

  //TODO: JavaDoc this boi
  public PacketResponse(boolean accepted, String multiAddress) {
    packetID = PacketID.RESPONSE.getID();
    this.accepted = accepted;
    this.multiAddress = multiAddress;
    data = packetID + "," + Boolean.toString(accepted) + "," + multiAddress;
  }
  /**
   * Constructs a packet from a string of data
   * @param data Packet data received from sender
   */
  public PacketResponse(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.accepted = Boolean.parseBoolean(unpackedData[1]);
    this.multiAddress = unpackedData[2];
  }

  public boolean isAccepted() {
    return accepted;
  }

  public String getMultiAddress() {
    return multiAddress;
  }
}
