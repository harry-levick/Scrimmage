package shared.gameObjects.objects;

import client.handlers.audioHandler.AudioHandler;
import client.handlers.effectsHandler.Colour;
import client.main.Client;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.shape.Rectangle;
import shared.gameObjects.Destructable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.components.behaviours.MovingPlatform;
import shared.gameObjects.players.Player;
import shared.physics.Physics;
import shared.physics.data.Collision;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class LaserBeam extends GameObject {

  boolean laserActive;
  final float TIME_BETWEEN_STATES = 1.7f;
  final float TIME_IN_LASER = 1.1f;
  float timer;
  BoxCollider bc;
  Rectangle laser;
  Colour colour;

  public LaserBeam(double x, double y, UUID uuid) {
    super(x, y, 80, 80, ObjectType.Bot, uuid);
    timer = TIME_BETWEEN_STATES;
    laserActive = false;
    bc = new BoxCollider(this, ColliderLayer.WALL, false);
    addComponent(bc);
    addComponent(new Rigidbody(0, this));
    colour = new Colour(255, 0, 0);
    addComponent(new MovingPlatform(this));
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/objects/laserEntity.png");
  }

  @Override
  public void update() {
    super.update();
    if(laser == null) {
      intialiseLaser();
    } else {
      recalculatePositions();
    }

    if(laserActive) {
      ArrayList<Collision> collisions = Physics.boxcastAll(new Vector2(laser.getX(), laser.getY()), new Vector2(laser.getWidth(), laser.getHeight()));
      for (Collision c : collisions) {
          if (c.getCollidedObject() instanceof Destructable) {
            ((Destructable) c.getCollidedObject()).deductHp(9999);
          }
      }
      colour.setR(colour.getR()-2);
      colour.setB(colour.getB()+1);
      laser.setOpacity(laser.getOpacity()-0.015);
      laser.setStyle("-fx-fill: " + colour.toHex() + ";");
      timer -= Physics.TIMESTEP;

      if(timer <= 0) {
        colour.setR(255);
        colour.setB(0);
        timer = TIME_BETWEEN_STATES;
        laserActive = false;
        laser.setOpacity(0);
      }

    } else {
      timer -= Physics.TIMESTEP;

      if(timer <= 0) {
        timer = TIME_IN_LASER;
        laserActive = true;
        laser.setOpacity(1);
        new AudioHandler(settings, Client.musicActive).playSFX("LASER");
      }
    }
  }

  void intialiseLaser() {
    laser = new Rectangle();
    laser.setOpacity(0);
    laser.setX(bc.getCorners()[1].getX() + bc.getSize().getX()*0.28f);
    laser.setY(bc.getCentre().getY());
    ArrayList<Collision> collisions = Physics.boxcastAll( new Vector2(laser.getX(), laser.getY()), new Vector2(transform.getSize().getX()*0.44f, 1080));
    float closestPoint = 1100;
    for (Collision c : collisions) {
      if(c.getCollidedObject().getComponent(ComponentType.RIGIDBODY) != null) {
        if (((Rigidbody) c.getCollidedObject().getComponent(ComponentType.RIGIDBODY) ).getBodyType() == RigidbodyType.STATIC && c.getCollidedObject() != this) {
          closestPoint = c.getPointOfCollision().getY() < closestPoint ? c.getPointOfCollision().getY() : closestPoint;
        }
      }
    }
    laser.setWidth(transform.getSize().getX()*0.44f);
    laser.setHeight(closestPoint - laser.getY());
    laser.setStyle("-fx-fill: " + colour.toHex() + ";");
    root.getChildren().add(1, laser);
  }

  void recalculatePositions() {
    laser.setX(bc.getCorners()[1].getX() + bc.getSize().getX()*0.28f);
    laser.setY(bc.getCentre().getY());
    ArrayList<Collision> collisions = Physics.boxcastAll( new Vector2(laser.getX(), laser.getY()), new Vector2(transform.getSize().getX()*0.44f, 1080));
    float closestPoint = 1100;
    for (Collision c : collisions) {
      if(c.getCollidedObject().getComponent(ComponentType.RIGIDBODY) != null) {
        if (((Rigidbody) c.getCollidedObject().getComponent(ComponentType.RIGIDBODY) ).getBodyType() == RigidbodyType.STATIC && c.getCollidedObject() != this) {
          closestPoint = c.getPointOfCollision().getY() < closestPoint ? c.getPointOfCollision().getY() : closestPoint;
        }
      }
    }
    laser.setWidth(transform.getSize().getX()*0.44f);
    laser.setHeight(closestPoint - laser.getY());
  }
}
