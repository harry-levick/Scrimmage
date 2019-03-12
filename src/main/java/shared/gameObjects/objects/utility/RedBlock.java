package shared.gameObjects.objects.utility;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.objects.ColourBlock;
import shared.gameObjects.objects.ColouredBlock;

public class RedBlock extends ColouredBlock {

  public RedBlock(double x, double y, double sizeX, double sizeY, ObjectType id, UUID exampleUUID) {
    super(x, y, sizeX, sizeY, id, exampleUUID);
    blockType = ColourBlock.RED;
  }

  @Override
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/objects/specialBlocks/redBlockDefault.png");
    this.animation.supplyAnimation("active", "images/objects/specialBlocks/redBlockActive.png");
  }
}
