package client.handlers.levelHandler;

import shared.gameObjects.GameObject;

import java.io.*;
import java.util.ArrayList;

public class MapLoader {

    public static void saveMap(ArrayList<GameObject> gameObjects, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameObjects);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<GameObject> loadMap(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<GameObject> gameObjects = (ArrayList<GameObject>)ois.readObject();
            return gameObjects;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    //TODO Replace with lambda
    //TODO Add map image and playlist
    public static ArrayList<Map> getMaps(String path) {
        File dir = new File(path);
        File files[] = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".ser");
            }
        });
        ArrayList<Map> maps = new ArrayList<>();
        for (File file : files) {
            Map tempMap = new Map(file.getName(), file.getPath(), GameState.IN_GAME);
            maps.add(tempMap);
        }
        return maps;
    }

}
