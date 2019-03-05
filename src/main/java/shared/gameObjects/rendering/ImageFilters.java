package shared.gameObjects.rendering;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;

public class ImageFilters {
  
  //Color adjust definitions 
  ColorAdjust greyScale = new ColorAdjust();
  ColorAdjust desaturate = new ColorAdjust();
  ColorAdjust empty = new ColorAdjust();

  public ImageFilters() {
    setGreyScale();
    setDesaturate(0.5);
    
  }
  
  
  private ColorAdjust getFilter(String filter) {
    if(filter.equals("greyscale")) {
      return greyScale;
    }
    else if(filter.equals("desaturate")) {
      return desaturate;
    }
    return empty;
  }
  
  private void setGreyScale() {
    greyScale.setSaturation(-1.0d);
    
  }
  
  public void setDesaturate(double amount) {
    greyScale.setSaturation(amount);
  }
  
  public void applyFilter(ImageView iv,String... filters) { //Used to set effect for individual imageView
    for (String f : filters) {
      iv.setEffect(getFilter(f));
    }
  }
  
  public void applyFilter(Group node,String...filters) { //Used to set effect for group (eg root, for all).
    for (String f : filters) {
      node.setEffect(getFilter(f));
    }
  }
    
  

}

