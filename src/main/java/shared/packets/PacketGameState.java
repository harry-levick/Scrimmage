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
  private int lastProcessedInput;


  public PacketGameState(ArrayList<GameObject> gameObjects, int lastProcessedInput) {
    packetID = PacketID.GAMESTATE.getID();
    data = "" + packetID + "," + lastProcessedInput;
    for (GameObject object : gameObjects) {
      data += "," + object.getState();
    }
  }

  public PacketGameState(String data) {
    gameObjects = new HashMap<>();
    List<String> dataFilter = new ArrayList<>(Arrays.asList(data.split(",")));
    dataFilter.removeAll(Collections.singleton("null"));
    String[] unpackedData = dataFilter.toArray(new String[0]);
    lastProcessedInput = Integer.parseInt(unpackedData[1]);
    for (int i = 2; i < unpackedData.length; i++) {
      String[] unpackedData2 = unpackedData[i].split(";");
      gameObjects.put(UUID.fromString(unpackedData2[0]), unpackedData[i]);
    }
  }

  public HashMap<UUID, String> getGameObjects() {
    return gameObjects;
  }

  public int getLastProcessedInput() {
    return lastProcessedInput;
  }
}
