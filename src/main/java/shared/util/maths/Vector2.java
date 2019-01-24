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

  public static Vector2 Up() {
    return new Vector2(0, 1);
  }

  public void mult(float scalar) {
    setX(getX() * scalar);
    setY(getY() * scalar);
  }

  public void mult(Vector2 vector) {
    setX(getX() * vector.getX());
    setY(getY() * vector.getY());
  }

  public void add(float scalar) {
    setX(getX() + scalar);
    setY(getY() + scalar);
  }

  public void add(Vector2 vector) {
    setX(getX() + vector.getX());
    setY(getY() + vector.getY());
  }

  public void sub(float scalar) {
    setX(getX() - scalar);
    setY(getY() - scalar);
  }

  public void sub(Vector2 vector) {
    setX(getX() - vector.getX());
    setY(getY() - vector.getY());
  }

  public void div(float scalar) {
    setX(getX() / scalar);
    setY(getY() / scalar);
  }

  public void div(Vector2 vector) {
    setX(getX() / vector.getX());
    setY(getY() / vector.getY());
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
   * Exact magnitude calculation, more computationally expensive but more precise, use when needed.
   *
   * @return Exact Magnitude of Vectors as float
   */
  public float exactMagnitude(Vector2 vector) {
    return (float)
        Math.sqrt(Math.pow(vector.getX() - getX(), 2) + Math.pow(vector.getY() - getY(), 2));
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

  /**
   * Angle of the vector with respect to world space
   */
  public float angle() {
    return angleBetween(Zero());
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
