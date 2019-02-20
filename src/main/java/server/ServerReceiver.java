package server;

import client.main.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.players.Player;
import shared.packets.PacketInput;
import shared.packets.PacketJoin;
import shared.packets.PacketReady;

public class ServerReceiver implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(Client.class.getName());
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
    int packetID = Integer.parseInt(message.split(",")[0]);
    if (packetID == 0 && server.playerCount.get() < 4
        && server.serverState == ServerState.WAITING_FOR_PLAYERS) {
      PacketJoin joinPacket = new PacketJoin(message);
      player = new Player(joinPacket.getX(), joinPacket.getY(),
          joinPacket.getClientID());
      Server.levelHandler.addPlayer(player, null);
      server.playerCount.getAndIncrement();
      connected.add(socket.getRemoteSocketAddress().toString());

      try {
        socket.setSoTimeout(5000);
      } catch (SocketException e) {
        e.printStackTrace();
      }

      /** Main Loop */
      while (true) {
        try {
          message = input.readLine();
        } catch (SocketTimeoutException e) {
          server.playerCount.decrementAndGet();
          connected.remove(socket.getRemoteSocketAddress().toString());
          server.levelHandler.getPlayers().remove(player);
          server.levelHandler.getGameObjects().remove(player);
          break;
        } catch (IOException e) {
          e.printStackTrace();
        }
        packetID = Integer.parseInt(message.split(",")[0]);
        System.out.println(message);
        switch (packetID) {
          case 2:
            PacketInput inputPacket = new PacketInput(message);
            if (inputPacket.getUuid() == player.getUUID()) {
              player.mouseY = inputPacket.getY();
              player.mouseX = inputPacket.getX();
              player.leftKey = inputPacket.isLeftKey();
              player.rightKey = inputPacket.isRightKey();
              player.jumpKey = inputPacket.isJumpKey();
              player.click = inputPacket.isClick();
            }
            break;
          case 5:
            PacketReady readyPacket = new PacketReady(message);
            if (readyPacket.getUUID() == player.getUUID()
                && server.serverState == ServerState.WAITING_FOR_PLAYERS
                || server.serverState == ServerState.WAITING_FOR_READYUP) {
              server.readyCount.getAndIncrement();
            }

        }
      }
    }
  }
}