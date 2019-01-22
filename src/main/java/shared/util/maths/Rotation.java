package shared.util.maths;

import java.io.Serializable;

public class Rotation implements Serializable {
    private float rot;

    public Rotation(float rot) {
        this.rot = rot;
    }

    public float getRot() {
        return rot;
    }

    public void setRot(float rot) {
        this.rot = rot;
    }
}
