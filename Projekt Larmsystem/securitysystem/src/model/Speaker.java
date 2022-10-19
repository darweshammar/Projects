package model;

/**@author Jens Moths, Per Blomqvist, Malek Abdul Sater  @coauthor**/
public class Speaker extends SecurityComponent {
    private boolean sound;

    public Speaker(String id, String location, boolean sound) {
        super(id, location);
        this.sound = sound;
    }

    public Speaker() {
        super("", "");
        sound = false;
    }
    public Speaker(String id, String location) {
        super(id, location);
        sound = false;
    }

    public boolean isSounding() {
        return sound;
    }

    public void setSounding(boolean sound) {
        this.sound = sound;
    }
}
