package shared.gameObjects.weapons;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;
import shared.util.Path;

public class TestPosition extends GameObject {

  private Player player;
  
  public TestPosition(double x, double y, UUID uuid, Player player) {
    super(x, y, 5, 5, ObjectID.Bullet, uuid);
    this.player = player;
  }
  
  @Override
  public void update() {
    if (player.getJumped() && player.getFacingLeft()) {  // when jumping and facing left
      this.setX(player.getHandLeftJumpX());
      this.setY(player.getHandLeftJumpY());
    }
    else if (player.getJumped() && player.getFacingRight()) { // when jumping and facing right
      this.setX(player.getHandRightJumpX());
      this.setY(player.getHandRightJumpY());
    }
    else if (player.getFacingLeft()) {  // when facing left
      this.setX(player.getHandLeftX());
      this.setY(player.getHandLeftY());
    }
    else if (player.getFacingRight()) { // when facing right
      this.setX(player.getHandRightX());
      this.setY(player.getHandRightY());
    }
    super.update();
  }
  
  @Override
  public void render() {
    super.render();
    imageView.setTranslateX(this.getX());
    imageView.setTranslateY(this.getY());
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimationWithSize("default", 20, 20, true, 
        Path.convert("images/weapons/bullet.png"));
  }
  
}
