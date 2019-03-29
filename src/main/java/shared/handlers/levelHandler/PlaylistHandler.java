package shared.handlers.levelHandler;

import client.main.Settings;
import java.util.ArrayList;
import java.util.UUID;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;

public class PlaylistHandler extends GameObject {

  private ArrayList<Playlist> playlists;
  private transient Pane[] panes;
  public PlaylistHandler(UUID uuid) {
    super(0, 0, 1, 1, ObjectType.Button, uuid);
  }

  @Override
  public void initialise(Group root, Settings settings) {
    super.initialise(root, settings);
    panes = new Pane[2];
    panes[0] = new Pane();
    panes[1] = new Pane();
    initPlaylistTab();
    root.getChildren().addAll(panes);
  }

  private void initPlaylistTab() {
    TableView<Map> tableView = new TableView();
    TableColumn<Map, String> mapNames = new TableColumn("Name");
    tableView.setPrefWidth(800);

    tableView.getColumns().addAll(mapNames);
    panes[0].getChildren().add(tableView);
    panes[0].setTranslateX(200);
    panes[0].setTranslateY(200);
  }

  private void initMapTab() {

  }
}
