package client.handlers.connectionHandler;

import client.main.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import shared.packets.Packet;
import shared.packets.PacketJoin;
import shared.packets.Socket;

public class ConnectionHandler extends Thread {

  public BlockingQueue received;


  private InetAddress address;
  private DatagramSocket socket;
  private byte[] buffer;
  private int port;
  private boolean connected;
  private Socket multicastSocket;


  public ConnectionHandler(String address) {
    multicastSocket = new Socket();
    connected = true;
    port = 4445;
    received = new LinkedBlockingQueue<String>();
    try {
      this.socket = new DatagramSocket();
      this.address = InetAddress.getByName("192.168.0.13");
    } catch (SocketException | UnknownHostException e) {
      e.printStackTrace();
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
    DatagramPacket sendPacket = new DatagramPacket(joinPacket.getData(),
        joinPacket.getData().length, address, port);
    try {
      socket.send(sendPacket);
      System.out.println("sent");
    } catch (IOException e) {
      e.printStackTrace();
    }
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
        multicastSocket.get().receive(packet);
        System.out.println(Arrays.toString(packet.getData()));
        received.add(Arrays.toString(packet.getData()));
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          multicastSocket.get().leaveGroup(multicastSocket.getMulticastAddress());
          multicastSocket.get().close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    try {
      multicastSocket.get().leaveGroup(multicastSocket.getMulticastAddress());
    } catch (IOException e) {
      e.printStackTrace();
    }
    multicastSocket.get().close();
  }

  public void end() {
    connected = false;
  }


  public void send(byte[] data) {
    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
    try {
      socket.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
