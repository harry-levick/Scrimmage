package shared.packets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class SocketMulti {

  private NetworkInterface networkInterface;
  private InetAddress ip;
  private InetAddress multicastAddress;
  private MulticastSocket multicastSocket;
  private int multicastPort;

  public SocketMulti() {
    multicastPort = 4446;
    try {
      multicastAddress = InetAddress.getByName("230.0.0.0");
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

    Enumeration<NetworkInterface> enumNetworkInterfaces = null;
    try {
      enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      e.printStackTrace();
    }
    while (enumNetworkInterfaces.hasMoreElements()) {

      networkInterface = enumNetworkInterfaces.nextElement();
      Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();

      while (enumInetAddress.hasMoreElements()) {
        InetAddress inetAddress = enumInetAddress.nextElement();

        if (inetAddress.isSiteLocalAddress()) {
          ip = inetAddress;
          break;
        }
      }
      if (ip != null) {
        break;
      }
    }

    try {
      this.multicastSocket = new MulticastSocket(multicastPort);
      multicastSocket.setInterface(ip);
      multicastSocket.setBroadcast(true);
      multicastSocket
          .joinGroup(new InetSocketAddress(multicastAddress, multicastPort), networkInterface);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public MulticastSocket get() {
    return multicastSocket;
  }

  public InetAddress getMulticastAddress() {
    return multicastAddress;
  }

  public int getMulticastPort() {
    return multicastPort;
  }
}
