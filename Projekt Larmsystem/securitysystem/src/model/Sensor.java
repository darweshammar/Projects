package model;

/**@author Jens Moths, Per Blomqvist, Malek Abdul Sater  @coauthor**/
public class Sensor extends SecurityComponent{
    private boolean active;

    public Sensor(String id, String location, boolean active) {
        super(id, location);
        this.active = active;
    }

    public Sensor(String id, String location) {
        super(id, location);
        active = true;
    }
    public Sensor() {
        super("", "");
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isOpen() {
        return super.isOpen();
    }

    @Override
    public void setOpen(boolean open) {
        super.setOpen(open);
    }
}
