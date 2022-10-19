package model;

/**@author Jens Moths, Per Blomqvist, Malek Abdul Sater  @coauthor**/
public class FireAlarm extends Sensor {


    public FireAlarm(String id, String location, boolean active) {
        super(id, location, active);
    }
    public FireAlarm(String id, String location) {
        super(id, location);
    }

    public FireAlarm() {
        super("", "", false);
    }

}
