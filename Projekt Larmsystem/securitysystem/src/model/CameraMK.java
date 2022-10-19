package model;

/**@author Per Blomqvist  @coauthor**/
public class CameraMK extends SecurityComponent {

    public CameraMK(String id, String location, boolean open) {
        super(id, location);
        super.setOpen(open);
    }
}
