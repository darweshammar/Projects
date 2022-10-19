package model;

/**@author Jens Moths, Per Blomqvist, Malek Abdul Sater  @coauthor**/
public class MagneticSensor extends Sensor {

    public MagneticSensor(String id, String location, boolean active) {
        super(id, location, active);
    }
    public MagneticSensor(String id, String location) {
        super(id, location);
    }
    public MagneticSensor() {
        super("", "", false);
    }
    public MagneticSensor(boolean open) {
        super("", "", false);
        super.setOpen(open);
    }
}
