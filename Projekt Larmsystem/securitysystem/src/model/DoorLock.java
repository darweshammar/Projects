package model;

/**@author Jens Moths, Per Blomqvist, Malek Abdul Sater  @coauthor**/
public class DoorLock extends SecurityComponent {



    public DoorLock(String id, String location, boolean open) {
        super(id, location);
        super.setOpen(open);
    }

    public DoorLock(String id, String location) {
        super(id, location);


    }

    public DoorLock(boolean open) {
        super("", "");
        super.setOpen(open);
    }



    @Override
    public String toString() {
        if (super.isOpen()) {
            return getClass().getSimpleName() + "   Id: " + super.getId() + "   Location: " + super.getLocation()+ " Dörren är : Öppen";
        } else return getClass().getSimpleName() + "   Id: " + super.getId() + "   Location: " + super.getLocation() + " Dörren är : Stängd";
    }
}
