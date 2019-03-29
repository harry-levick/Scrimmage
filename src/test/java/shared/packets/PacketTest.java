package shared.packets;

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.Test;
import shared.gameObjects.GameObject;
import shared.gameObjects.TestObject;

public class PacketTest {

  private double x = 10;
  private double y = 213.12;
  private boolean leftKey = false;
  private boolean rightKey = true;
  private boolean jumpKey = true;
  private boolean click = false;
  private boolean throwHolding = true;
  private String username = "TheBigMJ";
  private UUID uuid = UUID.randomUUID();


  @Test
  public void InputPacketTest() {
    PacketInput input = new PacketInput(x, y, leftKey, rightKey, jumpKey, click, throwHolding, uuid,
        0);
    PacketInput output = new PacketInput(input.getString());
    assertEquals(PacketID.INPUT.getID(), output.packetID);
    assertEquals(output.getX(), x);
    assertEquals(output.getY(), y);
    assertEquals(output.isLeftKey(), leftKey);
    assertEquals(output.isRightKey(), rightKey);
    assertEquals(output.isJumpKey(), jumpKey);
    assertEquals(output.isClick(), click);
    assertEquals(output.isThrowKey(), throwHolding);
  }


  @Test
  public void PlayerJoinPacketTest() {
    PacketJoin out = new PacketJoin(uuid, username, x, y, UUID.randomUUID(), UUID.randomUUID(),
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
        UUID.randomUUID(), UUID.randomUUID());
    byte[] packetData = out.getData();
    PacketJoin in = new PacketJoin(new String(packetData));
    assertEquals(PacketID.PLAYERJOIN.getID(), in.packetID);
    assertEquals(in.getX(), x);
    assertEquals(in.getY(), y);
    assertEquals(in.getUsername(), username);
    assertEquals(in.getClientID(), uuid);
  }

  @Test
  public void GameStatePacketTestSendAll() {
    ArrayList<GameObject> objects = new ArrayList<>();
    objects.add(new TestObject());
    objects.add(new TestObject());

    Packet out = new PacketGameState(objects, true);
    byte[] packetData = out.getData();
    PacketGameState in = new PacketGameState(new String(packetData));
    assertEquals(PacketID.GAMESTATE.getID(), in.packetID);
    assertEquals(objects.size(), in.getGameObjects().size());
  }

  @Test
  public void GameStatePacketTestSendUpdated() {
    ArrayList<GameObject> objects = new ArrayList<>();
    objects.add(new TestObject());
    objects.add(new TestObject());

    Packet out = new PacketGameState(objects, false);
    byte[] packetData = out.getData();
    PacketGameState in = new PacketGameState(new String(packetData));
    assertEquals(PacketID.GAMESTATE.getID(), in.packetID);
    assertEquals(0, in.getGameObjects().size());
  }
}
