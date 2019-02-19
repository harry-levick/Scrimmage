package shared.packets;

public class PacketEnd extends Packet {

  public PacketEnd() {
    packetID = PacketID.END.getID();
    data = Integer.toString(packetID);
  }

  public PacketEnd(String data) {
    String[] unpackedData = data.split(",");
    this.packetID = Integer.parseInt(unpackedData[0]);
  }
}
