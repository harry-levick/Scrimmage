package shared.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import shared.gameObjects.GameObject;

public class PacketGameState extends Packet {

  private HashMap<UUID, String> gameObjects;


  public PacketGameState(ArrayList<GameObject> gameObjects) {
    packetID = PacketID.GAMESTATE.getID();
    String dataString = "" + packetID;
    for (GameObject object : gameObjects) {
      dataString += "," + object.getState();
    }
    data = dataString.getBytes();
  }

  public PacketGameState(String data) {
    String[] unpackedData = data.split(",");
    for (int i = 1; i < unpackedData.length; i++) {
      String[] unpackedData2 = unpackedData[i].split(";");
      gameObjects.put(UUID.fromString(unpackedData2[0]), unpackedData[i]);
    }
  }

  public HashMap<UUID, String> getGameObjects() {
    return gameObjects;
  }
}
