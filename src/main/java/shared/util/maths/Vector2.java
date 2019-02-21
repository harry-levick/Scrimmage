package shared.util.maths;

import java.io.Serializable;

/**
 * @author fxa579 Base class for Vector mathematics in 2-Dimensions
 */
public class Vector2 implements Serializable {

  private float x;
  private float y;

  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public static Vector2 Zero() {
    return new Vector2(0, 0);
  }

  public static Vector2 Unit() {
    return new Vector2(1, 1);
  }

  public static Vector2 Right() {
    return new Vector2(1, 0);
  }

  public static Vector2 Left() {
    return new Vector2(-1, 0);
  }

  public static Vector2 Down() {
    return new Vector2(0, 1);
  }

  public static Vector2 max(Vector2 a, Vector2 b) {
    return a.magnitude() > b.magnitude() ? a : b;
  }

  public static Vector2 Up() {
    return new Vector2(0, -1);
  }

  public Vector2 mult(float scalar) {
    float x, y;
    x = (getX() * scalar);
    y = (getY() * scalar);
    return new Vector2(x, y);
  }

  public Vector2 mult(Vector2 vector) {
    float x, y;
    x = (getX() * vector.getX());
    y = (getY() * vector.getY());
    return new Vector2(x, y);
  }

  public Vector2 add(float scalar) {
    float x, y;
    x = (getX() + scalar);
    y = (getY() + scalar);
    return new Vector2(x, y);
  }

  public Vector2 add(Vector2 vector) {
    float x, y;
    x = (getX() + vector.getX());
    y = (getY() + vector.getY());
    return new Vector2(x, y);
  }

  public Vector2 sub(float scalar) {
    float x, y;
    x = (getX() - scalar);
    y = (getY() - scalar);
    return new Vector2(x, y);
  }

  public Vector2 sub(Vector2 vector) {
    float x, y;
    x = (getX() - vector.getX());
    y = (getY() - vector.getY());
    return new Vector2(x, y);
  }

  public Vector2 div(float scalar) {
    float x, y;
    x = (getX() / scalar);
    y = (getY() / scalar);
    return new Vector2(x, y);
  }

  public Vector2 div(Vector2 vector) {
    float x, y;
    x = getX() / vector.getX();
    y = getY() / vector.getY();
    return new Vector2(x, y);
  }

  /**
   * Compute the dot product of two vectors
   *
   * @return The dot product of two vectors
   */
  public float dot(Vector2 vector) {
    return getX() * vector.getX() + getY() * vector.getY();
  }

  /**
   * Approximates magnitude between two vectors
   *
   * @return Approximated Magnitude of Vectors as float
   */
  public float magnitude(Vector2 vector) {
    float a = 0.96f,
        b = 0.4f,
        ax = Math.abs(vector.getX() - getX()),
        by = Math.abs(vector.getY() - getY());
    return a * (Math.max(ax, by)) + b * (Math.min(ax, by));
  }

  /**
   * Approximates the magnitude of a vector
   *
   * @return Approximated Magnitude of Vectors as float
   */
  public float magnitude() {
    return magnitude(Vector2.Zero());
  }

  /**
   * Exact magnitude calculation, more computationally expensive but more precise, use when needed.
   *
   * @return Exact Magnitude of Vectors as float
   */
  public float exactMagnitude(Vector2 vector) {
    return (float)
        // Math.sqrt(Math.pow(vector.getX() - getX(), 2) + Math.pow(vector.getY() - getY(), 2));
        magnitude(vector);
  }

  /**
   * Angle between two vectors, approximated
   */
  public float angleBetween(Vector2 vector) {
    if (vector.magnitude(Zero()) == 0) {
      return (float) Math.tan(getY() / getX());
    }
    return (float) Math.acos(dot(vector) / (magnitude(Zero()) * vector.magnitude(Zero())));
  }

  /** Angle of the vector with respect to world space */
  public float angle() {
    return angleBetween(Zero());
  }

  /**
   * Computes the cross product between two vectors.
   */
  public Vector2 cross(Vector2 vector) {
    float x, y;
    x = getX() * vector.getY();
    y = -1 * getY() * vector.getX();
    return new Vector2(x, y);
  }

  /** Clamps the vector between the two values */
  public Vector2 clamp(Vector2 min, Vector2 max) {
    float x, y;
    x = Math.max(min.getX(), Math.min(max.getX(), getX()));
    y = Math.max(min.getY(), Math.min(max.getY(), getY()));
    return new Vector2(x, y);
  }

  public Vector2 normalize() {
    return this.div(magnitude(Vector2.Zero()));
  }

  @Override
  public String toString() {
    return "X: " + getX() + " Y: " + getY();
  }

  public boolean equals(Vector2 vector) {
    return (vector.getX() == this.x && vector.getY() == this.y);
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setVec(float x, float y) {
    setX(x);
    setY(y);
  }
}
