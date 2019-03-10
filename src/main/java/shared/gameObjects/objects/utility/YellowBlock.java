package shared.gameObjects.objects.utility;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.objects.ColourBlock;
import shared.gameObjects.objects.ColouredBlock;

public class YellowBlock extends ColouredBlock {

  public YellowBlock(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID exampleUUID) {
    super(x, y, sizeX, sizeY, id, exampleUUID);
    blockType = ColourBlock.YELLOW;
  }

  @Override
  public void initialiseAnimation() {
    this.animation
        .supplyAnimation("default", "images/objects/specialBlocks/yellowBlockDefault.png");
    this.animation.supplyAnimation("active", "images/objects/specialBlocks/yellowBlockActive.png");
  }
}
