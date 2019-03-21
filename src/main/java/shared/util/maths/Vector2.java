package shared.util.maths;

import java.io.Serializable;

/**
 * @author fxa579 Base class for Vector mathematics in 2-Dimensions
 */
public class Vector2 implements Serializable {

  private float x;
  private float y;

  /**
   * Constructs a 2D Vector of floats
   * @param x X-Value of Vector
   * @param y Y-Value of Vector
   */
  public Vector2(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a 2D Vector of floats
   * @param x X-Value of Vector
   * @param y Y-Value of Vector
   */
  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }
  /**
   * Constructs a 2D Vector of floats
   * @param x  Y-Value of Vector
   * @param y  Y-Value of Vector
   */
  public Vector2(double x, double y) {
    this.x = (float) x;
    this.y = (float) y;
  }
  /**
   * Constructs a 2D Vector of floats from a class-generated string
   * @param vector the string obtains from toString
   */
  public Vector2(String vector) {
    Vector2 vector2 = fromString(vector);
    this.x = vector2.getX();
    this.y = vector2.getY();
  }

  /**
   * Constructs a zero vector.
   * @return (0,0)
   */
  public static Vector2 Zero() {
    return new Vector2(0, 0);
  }

  /**
   * Constructs a unit vector.
   * @return (1,1)
   */
  public static Vector2 Unit() {
    return new Vector2(1, 1);
  }
  /**
   * Constructs a unit vector pointing right.
   * @return (1,0)
   */
  public static Vector2 Right() {
    return new Vector2(1, 0);
  }
  /**
   * Constructs a unit vector pointing left.
   * @return (-1,0)
   */
  public static Vector2 Left() {
    return new Vector2(-1, 0);
  }
  /**
   * Constructs a unit vector pointing down.
   * @return (0,1)
   */
  public static Vector2 Down() {
    return new Vector2(0, 1);
  }
  /**
   * Constructs a unit vector pointing up.
   * @return (0,-1)
   */

  public static Vector2 Up() {
    return new Vector2(0, -1);
  }

  /**
   * Returns the Vector2 with the higher magnitude
   */
  public static Vector2 max(Vector2 a, Vector2 b) {
    return a.magnitude() > b.magnitude() ? a : b;
  }

  /**
   * Returns the Vector2 with the lower magnitude
   */
  public static Vector2 min(Vector2 a, Vector2 b) {
    return a.magnitude() > b.magnitude() ? b : a;
  }

  /**
   * Multiplies both elements in the vector with a scalar
   *
   * @return The new product vector
   */
  public Vector2 mult(float scalar) {
    float x, y;
    x = (getX() * scalar);
    y = (getY() * scalar);
    return new Vector2(x, y);
  }

  /**
   * Multiplies the corresponding elements in the vectors
   *
   * @param vector
   * @return The new product vector
   */
  public Vector2 mult(Vector2 vector) {
    float x, y;
    x = (getX() * vector.getX());
    y = (getY() * vector.getY());
    return new Vector2(x, y);
  }

  /**
   * Adds both elements in the vector with a scalar
   *
   * @param scalar
   * @return The new summed vector
   */
  public Vector2 add(float scalar) {
    float x, y;
    x = (getX() + scalar);
    y = (getY() + scalar);
    return new Vector2(x, y);
  }

  /**
   * Adds the corresponding elements in the vectors
   *
   * @param vector
   * @return The new summed vector
   */
  public Vector2 add(Vector2 vector) {
    float x, y;
    x = (getX() + vector.getX());
    y = (getY() + vector.getY());
    return new Vector2(x, y);
  }

  /**
   * Subtracts both elements in the vector with a scalar
   *
   * @param scalar
   * @return The new subtracted vector
   */
  public Vector2 sub(float scalar) {
    float x, y;
    x = (getX() - scalar);
    y = (getY() - scalar);
    return new Vector2(x, y);
  }

  /**
   * Subtracts the corresponding elements in the vectors
   *
   * @param vector
   * @return The new subtracted vector
   */
  public Vector2 sub(Vector2 vector) {
    float x, y;
    x = (getX() - vector.getX());
    y = (getY() - vector.getY());
    return new Vector2(x, y);
  }

  /**
   * Divides both elements in the vector with a scalar
   *
   * @param scalar
   * @return The new quotient vector
   */
  public Vector2 div(float scalar) {
    float x, y;
    x = (getX() / scalar);
    y = (getY() / scalar);
    return new Vector2(x, y);
  }

  /**
   * Divides the corresponding elements in the vectors
   *
   * @param vector
   * @return The new quotient vector
   */
  public Vector2 div(Vector2 vector) {
    float x, y;
    x = getX() / vector.getX();
    y = getY() / vector.getY();
    return new Vector2(x, y);
  }

  /**
   * Computes the dot product of two vectors
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
        Math.sqrt(Math.pow(vector.getX() - getX(), 2) + Math.pow(vector.getY() - getY(), 2));
  }

  /**
   * Angle between two vectors, approximated
   *
   * @return The angle from -90 to +90 between the vectors
   */
  public float angleBetween(Vector2 vector) {
    if (vector.magnitude() == 0) {
      return (float) Math.atan(getY() / getX());
    }
    return (float) Math.acos(dot(vector) / (magnitude() * vector.magnitude()));
  }

  /**
   * Angle of the vector with respect to world space
   *
   * @return The angle between 0 and 180 between the vector and the world space
   */
  public float angle() {
    return angleBetween(Zero());
  }

  /**
   * Computes the cross product between two vectors.
   *
   * @return a.x*b.y - a.y*b.x
   */
  public Vector2 cross(Vector2 vector) {
    float x, y;
    x = getX() * vector.getY();
    y = -1 * getY() * vector.getX();
    return new Vector2(x, y);
  }
  /**
   * Computes the cross product between a scalar and a vector
   *
   * @return a.x*s - a.y*s
   */
  public Vector2 cross(float s) {
    float x, y;
    x = getX() * s;
    y = -1 * getY() * s;
    return new Vector2(x, y);
  }

  /**
   * Clamps the vector between the two values
   *
   * @return Vector clamped between 2 bounds
   */
  public Vector2 clamp(Vector2 min, Vector2 max) {
    float x, y;
    x = Math.max(min.getX(), Math.min(max.getX(), getX()));
    y = Math.max(min.getY(), Math.min(max.getY(), getY()));
    return new Vector2(x, y);
  }

  /**
   * Noramlizes the vector
   *
   * @return Normalized vector
   */
  public Vector2 normalize() {
    return this.div(magnitude());
  }

  /**
   * Computes the normal vector of the vector
   *
   * @return The normal corresponding to this vector
   */
  public Vector2 normal() {
    return new Vector2(-y, x);
  }

  /**
   * Measures the overlap distance assuming both vectors are in a 1D space
   */
  public float overlap(Vector2 projection) {
    return (y - projection.getX());
  }

  public boolean canOverlap(Vector2 projection) {
    return (!(projection.getY() < this.x || this.y < projection.getX()));
  }

  /**
   * Rotates the vector by the corresponding amount
   *
   * @param rotation The amount IN DEGREES to rotate the vector
   * @return New points of rotated vector
   */
  public Vector2 applyRotation(float rotation) {
    float angle = (float) Math.toRadians(rotation);
    return new Vector2(
        x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
  }

  @Override
  public String toString() {
    return "X:" + getX() + ":Y:" + getY();
  }

  private Vector2 fromString(String vector) {
    String[] split = vector.split(":");
    return new Vector2(Float.parseFloat(split[1]), Float.parseFloat(split[3]));
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
