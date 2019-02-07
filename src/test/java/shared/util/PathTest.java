package shared.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import org.junit.Test;

public class PathTest {

  @Test
  public void parse() {
    String output = Path.parse("images", "player", "walking.png");
    System.out.println(output);
    assertEquals(output, "images" + File.separator + "player" + File.separator + "walking.png");
  }
}
