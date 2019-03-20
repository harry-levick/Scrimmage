package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.players.Player;
import shared.packets.PacketInput;
import shared.packets.PacketJoin;
import shared.packets.PacketReady;

public class ServerReceiver implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(ServerReceiver.class.getName());
  private Player player;
  private Socket socket;
  private Server server;
  private ServerSocket serverSocket;
  private List connected;

  public ServerReceiver(Server server, ServerSocket serverSocket, List connected) {
    this.server = server;
    this.serverSocket = serverSocket;
    this.connected = connected;
  }

  @Override
  public void run() {
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
    System.out.println("Receivered" + message);
    int packetID = Integer.parseInt(message.split(",")[0]);
    if (packetID == 0
        && server.playerCount.get() < 4
        && server.serverState == ServerState.WAITING_FOR_PLAYERS) {
      PacketJoin joinPacket = new PacketJoin(message);
      Platform.runLater(
          () -> {
            player = server.addPlayer(joinPacket, socket.getInetAddress());
            server.sendObjects(server.getLevelHandler().getGameObjectsFiltered());
          }
      );

      System.out.println("test1");
      /** Main Loop */
      while (true) {
        try {
          System.out.println("test2");
          message = input.readLine();
          System.out.println("GOT" + message);
        } catch (SocketTimeoutException e) {
          server.playerCount.decrementAndGet();
          connected.remove(socket.getInetAddress());
          server.levelHandler.getPlayers().remove(player);
          server.levelHandler.getGameObjects().remove(player);
          LOGGER.debug("Removing player");
          System.out.println("test3");
          break;
        } catch (IOException e) {
          e.printStackTrace();
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
        }
      }
    }
  }
}
