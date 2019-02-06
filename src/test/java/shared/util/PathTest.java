package shared.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class PathTest {

  @Test
  public void parse() {
    String output = Path.parse("images", "player", "walking.png");
    System.out.println(output);
    assertEquals(output, "images" + File.separator + "player" + File.separator + "walking.png");
  }
}
