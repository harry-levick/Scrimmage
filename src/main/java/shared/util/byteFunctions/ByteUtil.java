package shared.util.byteFunctions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class ByteUtil {

  public static byte[] getBytesUUID(UUID uuid) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  public static UUID getUUID(byte[] bytes) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    Long high = byteBuffer.getLong();
    Long low = byteBuffer.getLong();
    return new UUID(high, low);
  }

  public static byte[] combinedBytes(byte[]... args) {
    byte[] sep = ",".getBytes();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    for (byte[] array : args) {
      try {
        stream.write(array);
        stream.write(sep);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return stream.toByteArray();
  }
}
