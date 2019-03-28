package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.players.Player;
import shared.packets.PacketInput;
import shared.packets.PacketJoin;
import shared.packets.PacketReSend;
import shared.packets.PacketReady;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

/**
 * Thread that reads Client sent data for the Server
 */
public class ServerReceiver implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(ServerReceiver.class.getName());
  private Player player;
  private Socket socket;
  private Server server;
  private ServerSocket serverSocket;
  private List connected;

  /**
   * Constructor:
   * @param server Current active server
   * @param serverSocket Socket to listen from
   * @param connected List of connected players
   */
  public ServerReceiver(Server server, ServerSocket serverSocket, List connected) {
    this.server = server;
    this.serverSocket = serverSocket;
    this.connected = connected;
  }

  @Override
  public void run() {
    boolean running = true;
    while (running) {
      String message = null;
      BufferedReader input = null;
      try {
        socket = this.serverSocket.accept();
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        /** Client Join */
        message = input.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      LOGGER.debug("Recieved Join request" + message);
      int packetID = Integer.parseInt(message.split(",")[0]);
      if (packetID == 0) {
        PacketJoin joinPacket = new PacketJoin(message);
        Platform.runLater(
            () -> {
              player = server.addPlayer(joinPacket, socket.getInetAddress());
            }
        );
        Thread.currentThread().setName("Server Receiver " + joinPacket.getUsername());

        /** Main Loop */
        while (true) {
          try {
            message = input.readLine();
            LOGGER.debug("GOT" + message);
          } catch (IOException e) {
            //Disconnect
            server.levelHandler.getPlayers().remove(player);
            server.levelHandler.getGameObjects().remove(player);
            player.removeRender();
            connected.remove(socket.getInetAddress());
            //Replacement bot
            Platform.runLater(
                () -> {
                  Bot botPlayer = new Bot(player.getX(), player.getY(), player.getUUID(),
                      server.settings.getLevelHandler());
                  botPlayer.initialise(server.settings.getGameRoot(), server.settings);
                  server.settings.getLevelHandler().getPlayers()
                      .put(botPlayer.getUUID(), botPlayer);
                  server.settings.getLevelHandler().getBotPlayerList()
                      .put(botPlayer.getUUID(), botPlayer);
                  server.settings.getLevelHandler().getGameObjects()
                      .put(botPlayer.getUUID(), botPlayer);
                  botPlayer.startThread();
                });
            System.out.println("Removing player and Swap for bot");
            running = false;
            break;
          }
          packetID = Integer.parseInt(message.split(",")[0]);
          switch (packetID) {
            //Player Input
            case 2:
              PacketInput inputPacket = new PacketInput(message);
              // Change to add to list
              server.getQueue(player).add(inputPacket);
              break;
            //Player Ready
            case 5:
              PacketReady readyPacket = new PacketReady(message);
              if (readyPacket.getUUID() == player.getUUID()
                  && server.serverState == ServerState.WAITING_FOR_PLAYERS
                  || server.serverState == ServerState.WAITING_FOR_READYUP) {
                server.readyCount.getAndIncrement();
              }
              break;
            case 9:
              PacketReSend packetReSend = new PacketReSend(message);
              GameObject gameObject = Server.getLevelHandler().getGameObjects()
                  .get(packetReSend.getGameobject());
              if (gameObject != null) {
                ConcurrentLinkedHashMap<UUID, GameObject> temp = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
                    .maximumWeightedCapacity(1).build();
                temp.put(gameObject.getUUID(), gameObject);
                server.sendObjects(temp);
              }
          }
        }
      }
    }
  }
}
