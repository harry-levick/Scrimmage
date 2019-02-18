package server;

import client.main.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.players.Player;
import shared.packets.PacketInput;
import shared.packets.PacketJoin;
import shared.packets.PacketReady;

public class ServerReceiver implements Runnable {

  public ConcurrentMap<UUID, Player> clientTable = new ConcurrentHashMap<>();
  private static final Logger LOGGER = LogManager.getLogger(Client.class.getName());
  private Thread t;
  private String threadName;
  private Player myPlayer;
  private DatagramSocket socket;
  private byte[] buffer = new byte[256];
  private Server server;

  public ServerReceiver(Server server) {
    this.server = server;
    threadName = "Server Receiver";
    try {
      socket = new DatagramSocket(4445);
    } catch (SocketException e) {
      e.printStackTrace();
      LOGGER.error("Error - Couldn't create socket in " + threadName);
    }
  }

  public void start() {
    LOGGER.debug("Starting " + threadName);
    if (t == null) {
      t = new Thread(this, threadName);
      t.start();
    }
  }

  @Override
  public void run() {
    while (true) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        socket.receive(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
      InetAddress address = packet.getAddress();
      int port = packet.getPort();
      packet = new DatagramPacket(buffer, buffer.length, address, port);
      String received = new String(packet.getData(), 0, packet.getLength());
      int packetID = Integer.parseInt(received.split(",")[0]);
      System.out.println(received);
      switch (packetID) {
        case 0:
          if (server.playerCount.get() < 4
              && server.serverState == ServerState.WAITING_FOR_PLAYERS) {
            PacketJoin joinPacket = new PacketJoin(received);
            Player newPlayer = new Player(joinPacket.getX(), joinPacket.getY(),
                joinPacket.getClientID());
            newPlayer.initialise(null);
            Server.levelHandler.addPlayer(newPlayer);
            clientTable.put(joinPacket.getClientID(), newPlayer);
            server.playerCount.getAndIncrement();
            /**
             PacketResponse responsePacket = new PacketResponse(true, "192.0.0.0");
             packet = new DatagramPacket(responsePacket.getData(), responsePacket.getData().length,
             address, port);
             try {
             socket.send(packet);
             } catch (IOException e) {
             e.printStackTrace();
             }
             **/
            System.out.println("test");
          }
          break;
        case 2:
          PacketInput inputPacket = new PacketInput(received);
          if (clientTable.containsKey(inputPacket.getUuid())) {
            Player player = clientTable.get(inputPacket.getUuid());
            player.mouseY = inputPacket.getY();
            player.mouseX = inputPacket.getX();
            player.leftKey = inputPacket.isLeftKey();
            player.rightKey = inputPacket.isRightKey();
            player.jumpKey = inputPacket.isJumpKey();
            player.click = inputPacket.isClick();
          }
          break;
        case 5:
          PacketReady readyPacket = new PacketReady(received);
          if (clientTable.containsKey(readyPacket.getUUID())
              && server.serverState == ServerState.WAITING_FOR_PLAYERS
              || server.serverState == ServerState.WAITING_FOR_READYUP) {
            server.readyCount.getAndIncrement();
          }

      }

    }
  }
}
