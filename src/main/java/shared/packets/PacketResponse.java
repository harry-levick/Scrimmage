package shared.packets;

public class PacketResponse extends Packet {

  private boolean accepted;
  private String multiAddress;

  public PacketResponse(boolean accepted, String multiAddress) {
    packetID = PacketID.RESPONSE.getID();
    this.accepted = accepted;
    this.multiAddress = multiAddress;
    data = packetID + "," + Boolean.toString(accepted) + "," + multiAddress;
  }

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
