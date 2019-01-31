package client.handlers.connectionHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionHandler extends Thread {

  public static BlockingQueue received;

  private InetAddress address;
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
      socket.joinGroup(this.address);
    } catch (IOException e) {
      connected = false;
      e.printStackTrace();
    }
    while (connected) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        socket.receive(packet);
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
