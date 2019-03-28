package client.handlers.effectsHandler;

import java.io.Serializable;

/**
 * Data container for RGB colour
 */
public class Colour implements Serializable {

  private int r, g, b;

  /**
   * Constructor
   *
   * @param r Red value (0-255)
   * @param g Green value (0-255)
   * @param b Blue value (0-255)
   */
  public Colour(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public int getR() {
    return r;
  }

  public void setR(int r) {
    if (r > 255) {
      r = 255;
    }
    if (r < 0) {
      r = 0;
    }
    this.r = r;
  }

  public int getG() {
    return g;
  }

  public void setG(int g) {
    if (g > 255) {
      g = 255;
    }
    if (g < 0) {
      g = 0;
    }
    this.g = g;
  }

  public int getB() {
    return b;
  }

  public void setB(int b) {
    if (b > 255) {
      b = 255;
    }
    if (b < 0) {
      b = 0;
    }
    this.b = b;
  }

  /**
   * Converts the current colour values to hex form
   *
   * @return String of colours in hex form
   */
  public String toHex() {
    return "#" + toHex(r) + toHex(g) + toHex(b);
  }

  private String toHex(int number) {
    StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
    while (builder.length() < 2) {
      builder.append("0");
    }
    return builder.toString().toUpperCase();
  }
}
