package shared.gameObjects.players;

import client.handlers.effectsHandler.Particle;
import client.handlers.effectsHandler.ServerParticle;
import client.main.Settings;
import java.util.UUID;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.players.Limbs.Arm;
import shared.gameObjects.players.Limbs.Body;
import shared.gameObjects.players.Limbs.Hand;
import shared.gameObjects.players.Limbs.Head;
import shared.gameObjects.players.Limbs.Leg;
import shared.gameObjects.rendering.ColourFilters;
import shared.gameObjects.rendering.PointLighting;
import shared.gameObjects.weapons.Punch;
import shared.gameObjects.weapons.Weapon;
import shared.handlers.levelHandler.GameState;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class Player extends GameObject {

  /**
   * The speed of the player in pixels per update frame
   */
  protected static final float speed = 9;
  /**
   * The jump force of the player in Newtons
   */
  protected static final float jumpForce = -300;
  /**
   * Max health
   */
  protected static final int maxHealth = 200;
  /**
   * Control Booleans determined by the Input Manager
   */
  public boolean leftKey, rightKey, jumpKey, click;
  //Testing
  public boolean deattach;
  /**
   * Boolean to determine when the "Throw Weapon" key is pressed down
   */
  public boolean throwHoldingKey;
  /**
   * Saves the current position of the cursor (X) and (Y)
   */
  public double mouseX, mouseY;
  /**
   * The score of the player used to determine win conditions
   */
  public int score;
  //TODO idk what this does
  protected Behaviour behaviour;
  /**
   * Boolean to determine if a player has jumped or not
   */
  protected boolean jumped;
  /**
   * Boolean to determine if a player is on the ground or in the air
   */
  protected boolean grounded;
  /**
   * True when the gun is aiming LHS
   */
  protected boolean aimLeft;
  /**
   * True when the mouse pointer is on the LHS
   */
  protected boolean pointLeft;
  /**
   * The current health of the player; is killed when reaches 0
   */
  protected int health;
  /**
   * The current weapon the player is using
   */
  protected Weapon holding;
  /**
   * When not holding a weapon, it defaults to the "Punch" weapon
   */
  protected Weapon myPunch;
  /**
   *
   */
  protected boolean damagedThisFrame;
  /**
   * The Physics Rigidbody component attached to the player
   */
  protected Rigidbody rb;
  /**
   * The Box collider that detects collisions
   */
  private BoxCollider bc;
  /**
   * If the lighting is on or off
   */
  private boolean lightingSwitch;

  /**
   * Players limbs
   */
  private Limb head;
  private Limb body;
  private Limb legLeft;
  private Limb legRight;
  private Limb armLeft;
  private Limb armRight;
  private Limb handLeft;
  private Limb handRight;
  /**
   * Synchronise the animations for each limb.
   */
  private int animationTimer = 0;


  /**
   * Networking count for each player of last received input for reconciliation
   */
  private int lastInputCount;

  //ColoFilter
  private transient ColourFilters colorFilter;

  // Lighting
  private transient PointLighting lighting;
  private transient ImageView lightingImageView;

  /**
   * Constructs a player object in the scene
   *
   * @param x The X-Coordinate of the object
   * @param y The Y-Coordinate of the object
   * @param playerUUID The uuid of the object
   */
  public Player(double x, double y, UUID playerUUID) {
    super(x, y, 80, 110, ObjectType.Player, playerUUID);
    this.lastInputCount = 0;
    this.score = 0;
    this.leftKey = false;
    this.rightKey = false;
    this.jumpKey = false;
    this.click = false;
    this.health = maxHealth;
    this.behaviour = Behaviour.IDLE;
    this.bc = new BoxCollider(this, ColliderLayer.PLAYER, false);
    this.rb = new Rigidbody(RigidbodyType.DYNAMIC, 90, 11.67f, 0.2f,
        new MaterialProperty(0f, 0.1f, 0.05f), null, this);
    addComponent(bc);
    addComponent(rb);
    aimLeft = pointLeft = true;
    lightingSwitch = false;
  }

  // Initialise the animation
  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player/player_idle.png");
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    addLimbs();
    addPunch();
    initialiseColorFilter();
    if (lightingSwitch) {
      initialiseLighting();
    }
  }


  /**
   * Server Side initaliser to allow limbs UUID's to be set same as client side
   *
   * @param root Game root to render character on
   * @param settings Settings
   * @param legLeftUUID UUID of left leg
   * @param legRightUUID UUID of right leg
   * @param bodyUUID UUID of body
   * @param headUUID UUID of head
   * @param armLeftUUID UUID of left arm
   * @param armRightUUID UUID of right arm
   * @param handLeftUUID UUID of left hand
   * @param handRightUUID UUID of right hand
   */
  public void initialise(Group root, Settings settings, UUID legLeftUUID, UUID legRightUUID,
      UUID bodyUUID, UUID headUUID, UUID armLeftUUID, UUID armRightUUID, UUID handLeftUUID,
      UUID handRightUUID) {
    super.initialise(root, settings);
    legLeft = new Leg(true, this, settings.getLevelHandler(), legLeftUUID);
    legRight = new Leg(false, this, settings.getLevelHandler(), legRightUUID);
    body = new Body(this, settings.getLevelHandler(), bodyUUID);
    head = new Head(this, settings.getLevelHandler(), headUUID);
    armLeft = new Arm(true, this, settings.getLevelHandler(), armLeftUUID);
    armRight = new Arm(false, this, settings.getLevelHandler(), armRightUUID);
    handLeft = new Hand(true, armLeft, this, settings.getLevelHandler(), handLeftUUID);
    handRight = new Hand(false, armRight, this, settings.getLevelHandler(), handRightUUID);
    addChild(legLeft);
    addChild(legRight);
    addChild(body);
    addChild(head);
    addChild(armLeft);
    addChild(armRight);
    armRight.addChild(handRight);
    armLeft.addChild(handLeft);
    addPunch();
    initialiseColorFilter();
    if (lightingSwitch) {
      initialiseLighting();
    }
  }

  private void initialiseColorFilter() {
    colorFilter = new ColourFilters();
    colorFilter.setDesaturate(0.0f);
  }

  private void initialiseLighting() {
    lighting = new PointLighting();
    lightingImageView = new ImageView();
    settings.getLevelHandler().getLightingRoot().getChildren().add(lightingImageView);
  }

  private void resetColorFilter() {
    colorFilter.setDesaturate(0.0f);
  }


  /**
   * Adds limbs to the player and creates them in level handler
   */
  private void addLimbs() {
    if (legLeft != null) {
      return;
    }
    legLeft = new Leg(true, this, settings.getLevelHandler(), UUID.randomUUID());
    legRight = new Leg(false, this, settings.getLevelHandler(), UUID.randomUUID());
    body = new Body(this, settings.getLevelHandler(), UUID.randomUUID());
    head = new Head(this, settings.getLevelHandler(), UUID.randomUUID());
    armLeft = new Arm(true, this, settings.getLevelHandler(), UUID.randomUUID());
    armRight = new Arm(false, this, settings.getLevelHandler(), UUID.randomUUID());
    handLeft = new Hand(true, armLeft, this, settings.getLevelHandler(), UUID.randomUUID());
    handRight = new Hand(false, armRight, this, settings.getLevelHandler(), UUID.randomUUID());
    addChild(legLeft);
    addChild(legRight);
    addChild(body);
    addChild(head);
    addChild(armLeft);
    addChild(armRight);
    armRight.addChild(handRight);
    armLeft.addChild(handLeft);
  }

  /**
   * Adds default weapon of punch to the player
   */
  private void addPunch() {
    myPunch = new Punch(getX(), getY(), "myPunch@Player", this, UUID.randomUUID());
    settings.getLevelHandler().addGameObject(myPunch);
    this.holding = myPunch;
  }

  @Override
  public void addChild(GameObject child) {
    super.addChild(child);
    settings.getLevelHandler().getLimbs().put(child.getUUID(), (Limb) child);
  }

  private void updateAnimationTimer() {
    if (this.behaviour != Behaviour.IDLE) {
      animationTimer++;
    } else {
      animationTimer = 0;
    }

  }

  public int getAnimationTimer() {
    return animationTimer;
  }

  private void applyFilter() {
    //Only start applying the desaturation when below 40% health.
    float hpc = getHealthPercentage();
    float ignitionLevel = 0.4f;
    if (hpc < ignitionLevel) {

      //Convert range of 0.0 -> 0.4 to -1 -> 0.
      float filterPercentage = (hpc / ignitionLevel) - 1;

      //Apply the filter percentage
      colorFilter.setDesaturate(filterPercentage);
      colorFilter.applyFilter(settings.getLevelHandler().getGameRoot(), "desaturate");
      colorFilter.applyFilter(settings.getLevelHandler().getBackgroundRoot(), "desaturate");
    }
  }

  private void updateLighting() {
    if (settings.getLevelHandler().getGameState() == GameState.IN_GAME) {
      lighting.update(getX(), getY());
      if (lightingImageView.getImage() == null) {
        lightingImageView.setImage(lighting.getImage());
      }
      lightingImageView.setX(lighting.getX());
      lightingImageView.setY(lighting.getY());
    } else {
      lightingImageView.setImage(null);
    }
  }

  @Override
  public void update() {
    // checks if outside the world, kills if fallen off the map
    if (getY() > 1200) {
      deductHp(999);
    }

    checkGrounded(); // Checks if the player is grounded
    badWeapon();
    pointLeft = mouseX < this.getX();
    updateAnimationTimer();
    if (deattach) {
      for (int i = 0; i < 6; i++) {
        Limb test = (Limb) children.get(i);
        test.detachLimb();
      }
    }
    damagedThisFrame = false;
    if (!(this instanceof Bot)) {
      applyFilter();
      if (lightingSwitch) {
        updateLighting();
      }
    }
    super.update();
  }

  @Override
  public String getState() {
    return objectUUID + ";" + id + ";" + getX() + ";" + getY() + ";" + animation.getName() + ";"
        + health + ";"
        + lastInputCount + ";"
        + throwHoldingKey + ";"
        + behaviour.name();
  }

  @Override
  public void setState(String data, Boolean snap) {
    super.setState(data, snap);
    String[] unpackedData = data.split(";");
    //this.animation.switchAnimation(unpackedData[4]);
    this.health = Integer.parseInt(unpackedData[5]);
    this.lastInputCount = Integer.parseInt(unpackedData[6]);
    this.throwHoldingKey = Boolean.parseBoolean(unpackedData[7]);
    this.behaviour = Behaviour.valueOf(unpackedData[8]);
  }

  /**
   * If the player is on the ground then set grounded
   */
  private void checkGrounded() {
    grounded = rb.isGrounded();
  }

  /**
   * Applies the inputs at the beginning of the frame for moving character and animation
   */
  public void applyInput() {
    if (grounded) {
      jumped = false;
    }
    if (rightKey) {
      rb.moveX(speed);
      createWalkParticle();
      behaviour = Behaviour.WALK_RIGHT;
    }
    if (leftKey) {
      rb.moveX(speed * -1);
      createWalkParticle();
      behaviour = Behaviour.WALK_LEFT;
    }

    if (!rightKey && !leftKey) {
      behaviour = Behaviour.IDLE;
    }
    if (jumpKey && !jumped && grounded) {
      rb.moveY(jumpForce * (legLeft.limbAttached && legRight.limbAttached ? 1f : 0.7f), 0.33333f);
      jumped = true;
    }
    if (jumped) {
      behaviour = Behaviour.JUMP;
    }

    if (grounded) {
      jumped = false;
    }
    if (throwHoldingKey) {
      this.throwHolding();
    }

    if (click && holding != null) {
      holding.fire(mouseX, mouseY);
    }
    // setX(getX() + (vx * 0.0166));
  }

  /**
   * Applies delayed input without creating unnecessary particles or objects
   */
  public void applyMultiplayerInput() {
    if (grounded) {
      jumped = false;
    }
    if (rightKey) {
      rb.moveX(speed);
      behaviour = Behaviour.WALK_RIGHT;
    }
    if (leftKey) {
      rb.moveX(speed * -1);
      behaviour = Behaviour.WALK_LEFT;
    }

    if (!rightKey && !leftKey) {
      behaviour = Behaviour.IDLE;
    }
    if (jumpKey && !jumped && grounded) {
      rb.moveY(jumpForce * (legLeft.limbAttached && legRight.limbAttached ? 1f : 0.7f), 0.33333f);
      jumped = true;
    }
    if (jumped) {
      behaviour = Behaviour.JUMP;
    }

    if (grounded) {
      jumped = false;
    }

  }

  /**
   * Creates particles on ground when walking
   */
  private void createWalkParticle() {
    if (!grounded || settings.isMultiplayer()) {
      return;
    }
    settings.getLevelHandler().addGameObject(
        new Particle(transform.getBotPos().sub(transform.getSize().mult(new Vector2(0.5, 0))),
            new Vector2(0, -35), new Vector2(0, 100), new Vector2(8, 8),
            "images/platforms/stone/elementStone001.png", 0.34f));
  }

  /**
   * Check if the current holding weapon is valid or not
   *
   * @return False if the weapon is a good weapon, or there is no weapon
   */
  public boolean badWeapon() {
    if (this.holding == null) {
      return false;
    }
    if (this.holding.getAmmo() == 0) {
      this.holding.destroyWeapon();
      this.setHolding(myPunch);
      return true;
    }
    try {
      if (!armLeft.limbAttached || !armRight.limbAttached) {
        this.throwHolding();
      }
    } catch (Exception e) {

    }
    return false;
  }

  /**
   * Throws the weapon currently held with a velocity
   */
  public void throwHolding() {
    if (!(this.holding == null || this.holding instanceof Punch)) {
      Weapon w = this.holding;
      w.startThrowing();
      throwHoldingKey = false;
      this.usePunch();
    }
  }

  /**
   * Players are not interpolated, unless networked
   *
   * @param deltaTimeAlpha calculation of delta time
   */
  @Override
  public void interpolatePosition(float deltaTimeAlpha) {

  }

  /**
   * Remove the image from the imageView by setting the image to null
   */
  @Override
  public void removeRender() {
    if (imageView != null) {
      imageView.setImage(null);
      Platform.runLater(
          () -> {
            root.getChildren().remove(imageView);
          }
      );
    }
    children.forEach(child -> child.removeRender());
  }

  /**
   * Applies damage to the player from weapons and other objects and manages players death
   *
   * @param damage Amount of damage to deal to player
   */
  public void deductHp(int damage) {
    if (!damagedThisFrame) {
      damagedThisFrame = true;
      this.health -= damage;
      if (this.health <= 0) {
        settings.playerDied();
        this.setActive(false);
        bc.setLayer(ColliderLayer.PARTICLE);
        children.forEach(child -> child.destroy());
      }
    }
  }

  /**
   * On skin change update all limbs with new rendered textures
   *
   * @param skinRender Textures of skin to apply
   */
  public void updateSkinRender(int[] skinRender) {
    children.forEach(child -> {
      if (child instanceof Arm) {
        ((Limb) child).updateSkinRender(skinRender[2]);
      }
      if (child instanceof Leg) {
        ((Limb) child).updateSkinRender(skinRender[3]);
      }
      if (child instanceof Head) {
        ((Limb) child).updateSkinRender(skinRender[0]);
      }
      if (child instanceof Body) {
        ((Limb) child).updateSkinRender(skinRender[1]);
      }
    });
  }


  /**
   * Resets the player's values, a "respawn"
   */
  public void reset() {
    health = maxHealth;
    if (this.active == false) {
      this.imageView.setRotate(0);
      this.imageView.setTranslateY(getY() - 70);
      this.setActive(true);
      this.bc.setLayer(ColliderLayer.PLAYER);
    }
    addPunch();
    resetColorFilter();

  }

  /**
   * Increases the player score by one unit
   */
  public void increaseScore() {
    score++;
  }

  /**
   * Increases the player score as per the game condition
   */
  public void increaseScore(int amount) {
    score += amount;
  }

  /**
   * Returns current health of player
   *
   * @return Health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Sets the health of player to specific value
   *
   * @param hp Health value to set player to
   */
  public void setHealth(int hp) {
    this.health = hp;
  }

  /**
   * Gets the base/max health of a player
   *
   * @return Max health
   */
  public int getMaxHealth() {
    return maxHealth;
  }

  /**
   * Gets health percentage for UI
   *
   * @return Health/Maxhealth
   */
  public float getHealthPercentage() {
    return (float) health / (float) maxHealth;
  }

  /**
   * Gets the current item that the player is holding
   *
   * @return Current item held, if null fist
   */
  public Weapon getHolding() {
    return holding == null ? myPunch : holding;
  }

  /**
   * Gives the player a new weapon to hold
   *
   * @param newHolding Weapon to give player to hold
   */
  public void setHolding(Weapon newHolding) {
    try {
      if (armLeft == null || armRight == null || !armLeft.limbAttached || !armRight.limbAttached) {
        return;
      }
    } catch (Exception e) {

    }
    this.holding = newHolding;

    if (newHolding != null) {
      newHolding.setSettings(settings);
      aimLeft = false;
    }
  }

  /**
   * Determines if the player can hold a weapon or not
   */
  public boolean canHold() {
    return !(!armLeft.limbAttached || !armRight.limbAttached);
  }

  /**
   * Sets holding to punch
   */
  public void usePunch() {
    this.setHolding(this.myPunch);
  }

  /**
   * Gets players current score in this game
   *
   * @return Players score
   */
  public int getScore() {
    return score;
  }

  /**
   * The back hand will be the main hand which holds the gun
   *
   * @return A 2 elements array, a[0] = X position of the hand, a[1] = Y position of the hand
   */
  public double[] getGunHandPos() {
    if (this.handLeft.isDeattached() || this.handRight.isDeattached()) {
      return new double[]{-1, -1};
    }
    if (isAimingLeft()) {
      return new double[]{this.handRight.getX(), this.handRight.getY()};
    } else {
      return new double[]{this.handLeft.getX(), this.handLeft.getY()};
    }
  }

  /**
   * The front facing hand will be the main hand which holds the melee
   *
   * @return A 2 elements array, a[0] = X position of the hand, a[1] = Y position of the hand
   */
  public double[] getMeleeHandPos() {
    if (isAimingLeft()) {
      if (handLeft.isDeattached()) {
        return new double[]{-1, -1};
      }
      return new double[]{this.handLeft.getX(), this.handLeft.getY()};
    } else {
      if (handRight.isDeattached()) {
        return new double[]{-1, -1};
      }
      return new double[]{this.handRight.getX(), this.handRight.getY()};
    }
  }

  public boolean containsChild(GameObject child) {
    for (GameObject c : children) {
      if (c.getUUID() == child.getUUID()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns if player is jumping
   *
   * @return Jumping
   */
  public boolean getJumped() {
    return this.jumped;
  }

  /**
   * Gets direction player is aiming their held item
   *
   * @return Aiming direction
   */
  public boolean isAimingLeft() {
    return this.aimLeft;
  }

  /**
   * Sets that the player is currently aiming left
   *
   * @param b Is aiming left
   */
  public void setAimingLeft(boolean b) {
    this.aimLeft = b;
  }

  /**
   * Checks if player is pointing cursor left
   *
   * @return Cursor left of player
   */
  public boolean isPointingLeft() {
    return this.pointLeft;
  }

  /**
   * Checks if the player is on the ground
   *
   * @return Is player grounded
   */
  public boolean isGrounded() {
    return grounded;
  }

  /**
   * Gets last input for networking received by this player
   *
   * @return Last Input number
   */
  public int getLastInputCount() {
    return lastInputCount;
  }

  /**
   * Sets input number of player
   *
   * @param lastInputCount Number of set last input to
   */
  public void setLastInputCount(int lastInputCount) {
    this.lastInputCount = lastInputCount;
  }

  /**
   * Gets the head of player
   *
   * @return Player Head
   */
  public Limb getHead() {
    return head;
  }

  /**
   * Gets the Body of the player
   *
   * @return Player Body
   */
  public Limb getBody() {
    return body;
  }

  /**
   * Gets the Left Hand of the player
   *
   * @return Player Left Hand
   */
  public Limb getHandLeft() {
    return handLeft;
  }

  /**
   * Gets the Right Hand of the player
   *
   * @return Player Right Hand
   */
  public Limb getHandRight() {
    return handRight;
  }

  /**
   * Gets the Left Leg of the player
   *
   * @return Player Left Leg
   */
  public Limb getLegLeft() {
    return legLeft;
  }

  /**
   * Gets the Right Leg of the player
   *
   * @return Player Right Leg
   */
  public Limb getLegRight() {
    return legRight;
  }

  /**
   * Gets the Left arm of the player
   *
   * @return Player Left Arm
   */
  public Limb getArmLeft() {
    return armLeft;
  }

  /**
   * Gets the Right Arm of the player
   *
   * @return Player Right Arm
   */
  public Limb getArmRight() {
    return armRight;
  }
}
