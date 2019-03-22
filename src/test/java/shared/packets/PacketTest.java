package shared.packets;

import static junit.framework.TestCase.assertEquals;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister.Pack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
  private String username = "TheBigMJ";
  private UUID uuid = UUID.randomUUID();

  @Test
  public void JoinPacketTest() {
    PacketJoin join = new PacketJoin(uuid, username, x, y);
    byte[] simulateNetwork = join.getData();
    PacketJoin output = new PacketJoin(new String(simulateNetwork));
    assertEquals(output.getClientID(), uuid);
    assertEquals(output.getUsername(), username);
  }

  @Test
  public void ResponsePacketTest() {
    boolean accepted = true;
    PacketResponse out = new PacketResponse(accepted, username);
    byte[] packetData = out.getData();
    PacketResponse in = new PacketResponse(new String(packetData));
    assertEquals(PacketID.RESPONSE.getID(), in.getPacketID());
    assertEquals(accepted, in.isAccepted());
    assertEquals(username, in.getMultiAddress());
  }

  @Test
  public void InputPacketTest() {
    PacketInput input = new PacketInput(x, y, leftKey, rightKey, jumpKey, click, uuid, 0);
    PacketInput output = new PacketInput(input.getString());
    assertEquals(PacketID.INPUT.getID(), output.packetID);
    assertEquals(output.getX(), x);
    assertEquals(output.getY(), y);
    assertEquals(output.isLeftKey(), leftKey);
    assertEquals(output.isRightKey(), rightKey);
    assertEquals(output.isJumpKey(), jumpKey);
    assertEquals(output.isClick(), click);
  }

  @Test
  public void MapPacketTest() {
    PacketMap out = new PacketMap(username, uuid);
    byte[] packetData = out.getData();
    PacketMap in = new PacketMap(new String(packetData));
    assertEquals(PacketID.MAP.getID(), in.getPacketID());
    assertEquals(username, in.getName());
    assertEquals(uuid, in.getUuid());
  }

  @Test
  public void PlayerJoinPacketTest() {
    PacketPlayerJoin out = new PacketPlayerJoin(x, y, uuid, username);
    byte[] packetData = out.getData();
    PacketPlayerJoin in = new PacketPlayerJoin(new String(packetData));
    assertEquals(PacketID.PLAYERJOIN.getID(), in.packetID);
    assertEquals(in.getX(), x);
    assertEquals(in.getY(), y);
    assertEquals(in.getUsername(), username);
    assertEquals(in.getUUID(), uuid);
  }

  @Test
  public void ReadyPacketTest() {
    PacketReady out = new PacketReady(uuid, username);
    byte[] packetData = out.getData();
    PacketReady in = new PacketReady(new String(packetData));
    assertEquals(PacketID.READY.getID(), in.packetID);
    assertEquals(in.getUUID(), uuid);
  }

  @Test
  public void EndPacketTest() {
    PacketEnd out = new PacketEnd();
    byte[] endData = out.getData();
    PacketEnd in = new PacketEnd(new String(endData));
    assertEquals(PacketID.END.getID(), in.packetID);
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
