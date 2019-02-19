package client.handlers.connectionHandler;

import client.main.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import shared.packets.Packet;
import shared.packets.PacketJoin;
import shared.packets.SocketMulti;

public class ConnectionHandler extends Thread {

  public BlockingQueue received;


  private byte[] buffer;
  private String address;
  private int port;
  private boolean connected;
  private SocketMulti multicastSocketMulti;
  private Socket socket;
  private PrintWriter out;


  public ConnectionHandler(String address) {
    multicastSocketMulti = new SocketMulti();
    connected = true;
    port = 4445;
    received = new LinkedBlockingQueue<String>();
    this.address = "192.168.0.13";
    try {
      socket = new Socket(address, port);
      out = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    //try {
      Packet joinPacket =
          new PacketJoin(
              Client.levelHandler.getClientPlayer().getUUID(), Client.settings.getUsername(),
              Client.levelHandler.getClientPlayer().getX(),
              Client.levelHandler.getClientPlayer().getY());
    send(joinPacket.getData());
    // socket.setSoTimeout(60000);
    //DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    /**
      socket.receive(packet);
      String response = Arrays.toString(packet.getData());
      if (Integer.parseInt(response.substring(0, 1)) == PacketID.RESPONSE.getID()) {
        PacketResponse responsePacket = new PacketResponse(response);
        if (responsePacket.isAccepted()) {
          addressRecieve = InetAddress.getByName(responsePacket.getMultiAddress());
          Client.multiplayer = true;
          // Client.levelHandler.changeMap(lobby)
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      connected = false;
      return;
    }
     **/
    Client.multiplayer = true;
    while (connected) {
      buffer = new byte[1024];
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        multicastSocketMulti.get().receive(packet);
        System.out.println(Arrays.toString(packet.getData()));
        received.add(Arrays.toString(packet.getData()));
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          multicastSocketMulti.get().leaveGroup(multicastSocketMulti.getMulticastAddress());
          multicastSocketMulti.get().close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    try {
      multicastSocketMulti.get().leaveGroup(multicastSocketMulti.getMulticastAddress());
    } catch (IOException e) {
      e.printStackTrace();
    }
    multicastSocketMulti.get().close();
  }

  public void end() {
    connected = false;
    out.close();
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void send(byte[] data) {
    out.println(data.toString());
  }
}
