package server;

import client.main.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.players.Player;
import shared.packets.PacketInput;
import shared.packets.PacketPlayerJoin;
import shared.packets.PacketReady;

public class ServerReceiver implements Runnable {

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
      LOGGER.error("Error - Couldn't create socket in " + threadName);
    }
  }

  public void start() {
    LOGGER.debug("Starting " + threadName);
    if (t == null) {
      t = new Thread(this, threadName);
      t.start();
      t.setDaemon(true);
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

      switch (packetID) {
        case 4:
          if (server.playerCount.get() < 4
              && server.serverState == ServerState.WAITING_FOR_PLAYERS) {
            PacketPlayerJoin joinPacket = new PacketPlayerJoin(received);
            Server.levelHandler.addPlayer(
                new Player(joinPacket.getX(), joinPacket.getY(), 80, 110, joinPacket.getUUID()));
            server.clientTable.put(joinPacket.getUUID(), new LinkedBlockingQueue<>());
            server.playerCount.getAndIncrement();
            //Send response to client
          }
          break;
        case 2:
          PacketInput inputPacket = new PacketInput(received);
          if (server.clientTable.containsKey(inputPacket.getUuid())) {
            server.clientTable.get(inputPacket.getUuid()).add(inputPacket);
          }
          break;
        case 5:
          PacketReady readyPacket = new PacketReady(received);
          if (server.clientTable.containsKey(readyPacket.getUUID())
              && server.serverState == ServerState.WAITING_FOR_PLAYERS
              || server.serverState == ServerState.WAITING_FOR_READYUP) {
            server.readyCount.getAndIncrement();
          }

      }

    }
  }
}
