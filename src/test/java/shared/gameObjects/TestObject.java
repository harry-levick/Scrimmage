package shared.gameObjects;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Version;

public class TestObject extends GameObject {
    /**
     * Base class used to create an object in game.
     * This is used on both the client and server side to ensure actions are calculated the same
     *
     * @param x       X coordinate of object in game world
     * @param y       Y coordinate of object in game world
     * @param id      Unique Identifier of every game object
     * @param version Is this object being created on server or client
     */
    public TestObject(int x, int y, ObjectID id, Version version) {
        super(x, y, id, version);
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }
}
