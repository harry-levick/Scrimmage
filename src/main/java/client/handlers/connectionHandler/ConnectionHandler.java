package client.handlers.connectionHandler;

import client.main.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import shared.packets.Packet;
import shared.packets.PacketID;
import shared.packets.PacketJoin;
import shared.packets.PacketResponse;

public class ConnectionHandler extends Thread {

  public BlockingQueue received;

  private InetAddress address;
  private InetAddress addressRecieve;
  private MulticastSocket socket;
  private byte[] buffer;
  private int port;
  private boolean connected;

  public ConnectionHandler(String address) {
    connected = true;
    port = 4446;
    buffer = new byte[256];
    received = new LinkedBlockingQueue<String>();
    try {
      this.socket = new MulticastSocket(port);
      this.address = InetAddress.getByName(address);
    } catch (SocketException | UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      Packet joinPacket =
          new PacketJoin(
              Client.levelHandler.getClientPlayer().getUUID(), Client.settings.getUsername());
      this.send(joinPacket.getData());
      socket.setSoTimeout(60000);
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
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
    try {
      socket.joinGroup(addressRecieve);
    } catch (IOException e) {
      connected = false;
      e.printStackTrace();
    }
    while (connected) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        socket.receive(packet);
        received.add(Arrays.toString(packet.getData()));
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          socket.leaveGroup(address);
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void send(byte[] data) {
    DatagramPacket packet = new DatagramPacket(data, data.length, address, 4445);
    try {
      socket.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
