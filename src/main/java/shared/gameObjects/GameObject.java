package shared.gameObjects;

import shared.ObjectID;

public abstract class GameObject {

    protected int x, y;
    protected ObjectID id;
    protected Version version;



    /**
     * Base class used to create an object in game.
     * This is used on both the client and server side to ensure actions are calculated the same
     *
     * @param x X coordinate of object in game world
     * @param y Y coordinate of object in game world
     * @param id Unique Identifier of every game object
     * @param version Is this object being created on server or client
     */
    public GameObject(int x, int y , ObjectID id, Version version) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.version = version;
    }

    //Server and Client side
    public abstract void update();

    //Client Side only
    public abstract void render();


    //Getters and Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ObjectID getId() {
        return id;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

}
