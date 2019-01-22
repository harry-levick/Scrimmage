package levelEditor;

import client.handlers.levelHandler.MapLoader;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import shared.gameObjects.ExampleObject;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Version;

import java.util.ArrayList;

public class Main extends Application {

    private ArrayList<GameObject> gameObjects;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Level Editor");
        Group root = new Group();
        gameObjects = new ArrayList<>();

        //Example of loading map
        gameObjects = MapLoader.loadMap("menus.map");
        gameObjects.forEach(gameObject -> gameObject.setupRender(root,new Image("images/platforms/stone/elementStone013.png"),Version.CLIENT));



        ChoiceBox cb = new ChoiceBox();
        cb.setItems(FXCollections.observableArrayList(
                "ExampleObject","Test2")
        );
        Button btn = new Button();
        btn.setText("Save Map");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MapLoader.saveMap(gameObjects,"menus.map");
            }
        });
        btn.setLayoutX(10);
        btn.setLayoutY(10);



        root.getChildren().add(cb);
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 1000, 1000);
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (cb.getValue() == "ExampleObject") {
                    GameObject temp = new ExampleObject(event.getX(),event.getY(), ObjectID.Bot);
                    temp.setupRender(root, new Image("images/platforms/stone/elementStone013.png"), Version.CLIENT);
                    gameObjects.add(temp);
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("testasd");

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameObjects.forEach(gameObject -> gameObject.render());
            }
            }.start();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
