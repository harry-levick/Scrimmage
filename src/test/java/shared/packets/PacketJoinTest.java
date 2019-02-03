package shared.packets;

import static junit.framework.TestCase.assertEquals;

import java.util.UUID;
import org.junit.Test;

public class PacketJoinTest {

  private UUID uuid = UUID.randomUUID();
  private String username = "TheBigMJ";

  @Test
  public void sameInSameOut() {
    PacketJoin join = new PacketJoin(uuid, username);
    byte[] simulateNetwork = join.getData();
    PacketJoin output = new PacketJoin(simulateNetwork);
    assertEquals(output.getClientID(), uuid);
    assertEquals(output.getUsername(), username);
  }

}