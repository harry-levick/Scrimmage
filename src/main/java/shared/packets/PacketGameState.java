package shared.packets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import shared.gameObjects.GameObject;

/**
 * Base packet for sending the game state from Server to Client
 */
public class PacketGameState extends Packet {

  private HashMap<UUID, String> gameObjects;
  private int lastProcessedInput;
  private boolean update = false;

  /**
   * Constructs a packet that can be sent to the client to update all the game objects in a map
   *
   * @param gameObjects The list of game objects available
   * @param sendAll Whether to send all data (true) or to only send data that has been updated in
   * the last frame (false)
   */
  public PacketGameState(ArrayList<GameObject> gameObjects, boolean sendAll) {
    packetID = PacketID.GAMESTATE.getID();
    data = "" + packetID + "," + lastProcessedInput;
    for (GameObject object : gameObjects) {
      if (object.getState() != "" && object.isNetworkStateUpdate() || sendAll) {
        update = true;
        data += "," + object.getState();
      }
    }
  }

  /**
   * Constructs a packet from received data to update a client's game objects
   *
   * @param data Packet data received from sender
   */
  public PacketGameState(String data) {
    gameObjects = new HashMap<>();
    List<String> dataFilter = new ArrayList<>(Arrays.asList(data.split(",")));
    dataFilter.removeAll(Collections.singleton("null"));
    dataFilter.removeAll(Collections.singleton(""));
    String[] unpackedData = dataFilter.toArray(new String[0]);
    this.packetID = Integer.parseInt(unpackedData[0]);
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

  public boolean isUpdate() {
    return update;
  }
}
