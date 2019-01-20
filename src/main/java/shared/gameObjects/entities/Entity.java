package shared.gameObjects.entities;

import shared.gameObjects.GameObject;
import shared.gameObjects.Version;

public abstract class Entity extends GameObject {

    /**
     * Base class used to create an object in game.
     * This is used on both the client and server side to ensure actions are calculated the same
     *
     * @param x       X coordinate of object in game world
     * @param y       Y coordinate of object in game world
     * @param id      Unique Identifier of every game object
     * @param version Is this object being created on server or client
     */
    public Entity(int x, int y, String id, Version version) {
        super(x, y, id, version);
    }
}
