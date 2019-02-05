package shared.packets;

public class PacketResponse extends Packet {

  private boolean accepted;
  private String mutliAddress;

  public PacketResponse(boolean accepted, String multiAddress) {
    packetID = PacketID.RESPONSE.getID();
    this.accepted = accepted;
    this.mutliAddress = multiAddress;
    data = (packetID + Boolean.toString(accepted) + multiAddress).getBytes();
  }

  public PacketResponse(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
    this.accepted = Boolean.parseBoolean(unpackedData[1]);
    this.mutliAddress = unpackedData[2];
  }

  public boolean isAccepted() {
    return accepted;
  }

  public String getMutliAddress() {
    return mutliAddress;
  }
}
