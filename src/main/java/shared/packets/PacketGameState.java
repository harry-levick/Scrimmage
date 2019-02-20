package shared.packets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import shared.gameObjects.GameObject;

public class PacketGameState extends Packet {

  private HashMap<UUID, String> gameObjects;


  public PacketGameState(ArrayList<GameObject> gameObjects) {
    packetID = PacketID.GAMESTATE.getID();
    data = "" + packetID;
    for (GameObject object : gameObjects) {
      data += "," + object.getState();
    }
  }

  public PacketGameState(String data) {
    gameObjects = new HashMap<>();
    List<String> dataFilter = new ArrayList<>(Arrays.asList(data.split(",")));
    dataFilter.removeAll(Collections.singleton("null"));
    String[] unpackedData = dataFilter.toArray(new String[0]);
    for (int i = 1; i < unpackedData.length; i++) {
      String[] unpackedData2 = unpackedData[i].split(";");
      gameObjects.put(UUID.fromString(unpackedData2[0]), unpackedData[i]);
    }
  }

  public HashMap<UUID, String> getGameObjects() {
    return gameObjects;
  }
}
