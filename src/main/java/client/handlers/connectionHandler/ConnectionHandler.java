package client.handlers.connectionHandler;

import client.main.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import shared.packets.Packet;
import shared.packets.PacketJoin;

public class ConnectionHandler extends Thread {

  public BlockingQueue received;

  private InetAddress multicastAddress;
  private InetAddress address;
  private MulticastSocket multicastSocket;
  private DatagramSocket socket;
  private byte[] buffer;
  private int multicastPort;
  private int port;
  private boolean connected;

  public ConnectionHandler(String address) {
    connected = true;
    multicastPort = 4446;
    port = 4445;
    received = new LinkedBlockingQueue<String>();
    try {
      this.multicastSocket = new MulticastSocket(multicastPort);
      this.socket = new DatagramSocket();
      this.address = InetAddress.getByName("localhost");
      this.multicastAddress = InetAddress.getByName("230.0.0.0");
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
              Client.levelHandler.getClientPlayer().getUUID(), Client.settings.getUsername(), Client.levelHandler.getClientPlayer().getX(), Client.levelHandler.getClientPlayer().getY());
      DatagramPacket sendPacket = new DatagramPacket(joinPacket.getData(), joinPacket.getData().length, address, port);
      try {
        socket.send(sendPacket);
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
    try {
      multicastSocket.joinGroup(multicastAddress);
    } catch (IOException e) {
      connected = false;
      e.printStackTrace();
    }
    while (connected) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        multicastSocket.receive(packet);
        received.add(Arrays.toString(packet.getData()));
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          multicastSocket.leaveGroup(multicastAddress);
          multicastSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
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
