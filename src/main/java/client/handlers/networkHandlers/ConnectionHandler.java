package client.handlers.networkHandlers;

import client.main.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Platform;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.Map;
import shared.packets.Packet;
import shared.packets.PacketJoin;
import shared.util.Path;

public class ConnectionHandler extends Thread {

  public BlockingQueue received;

  private byte[] buffer;
  private String address;
  private int port;
  private boolean connected;
  private DatagramSocket clientSocket;
  private Socket socket;
  private PrintWriter out;
  private int size;

  /**
   * Starts a new client side connection to a game server
   *
   * @param address Address of server to connect to
   */
  public ConnectionHandler(String address) {
    connected = true;
    size = 1000;
    port = 4446;
    received = new LinkedBlockingQueue<String>();
    this.address = address;
    try {
      clientSocket = new DatagramSocket(port);
      socket = new Socket(this.address, 4445);
      out = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      end();
    }
  }

  /**
   * Sends player join packet to the server to connect then begins listening to messages from server
   * and adding to received queue
   */
  public void run() {
    Player player = Client.levelHandler.getClientPlayer();
    Packet joinPacket =
        new PacketJoin(
            player.getUUID(),
            Client.settings.getUsername(),
            player.getX(),
            player.getY(),
            player.getLegLeft().getUUID(),
            player.getLegRight().getUUID(),
            player.getBody().getUUID(),
            player.getHead().getUUID(),
            player.getArmLeft().getUUID(),
            player.getArmRight().getUUID(),
            player.getHandLeft().getUUID(),
            player.getHandRight().getUUID(),
            player.getCurrentSkin());
    send(joinPacket.getString());

    Client.multiplayer = true;
    while (connected) {
      buffer = new byte[size];
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        clientSocket.receive(packet);
        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
        if (msg.startsWith("length:")) {
          size = Integer.parseInt(msg.split(":")[1]);
        } else if (msg.startsWith("object")) {
          packet = new DatagramPacket(buffer, buffer.length);
          clientSocket.receive(packet);
          ClientNetworkManager.createGameObjects(packet.getData());
        } else {
          received.add(msg.trim());
        }
      } catch (IOException e) {
        end();
      }
    }
  }

  /**
   * Ends connection to server
   */
  public void end() {
    if (connected) {
      connected = false;
      try {
        if (out != null) {
          out.close();
        }
        if (socket != null) {
          socket.close();
        }
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            Client.levelHandler.changeMap(
                new Map("menus/main_menu.map",
                    Path.convert("src/main/resources/menus/main_menu.map")),
                true, false);
            Client.multiplayer = false;
          }
        });
        Thread.currentThread().interrupt();
        return;
      }
    }
  }

  /**
   * Sends message to the server
   *
   * @param data Message to server
   */
  public void send(String data) {
    try {
      out.println(data);
    } catch (Exception e) {
      end();
    }
  }
}
