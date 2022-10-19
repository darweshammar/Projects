package model;

/**@author Per Blomqvist  @coauthor**/
public class FingerprintSensor extends Sensor {

    public FingerprintSensor(String id, String location, boolean active){
        super(id,location,active);
    }
    public FingerprintSensor(String id, String location){
        super(id,location);
    }

}
