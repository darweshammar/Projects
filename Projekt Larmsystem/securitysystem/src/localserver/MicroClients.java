package localserver;

import model.SecurityComponent;
import model.Sensor;


import java.io.Serializable;
import java.util.HashMap;

/**@author Rolf Axelsson  @coauthor Per Blomqvist**/
public class MicroClients implements Serializable {
    private HashMap<SecurityComponent, PiServer.ClientHandler> map = new HashMap<>();

    public synchronized void put(SecurityComponent sensor, PiServer.ClientHandler ch) {
        map.put(sensor, ch);
    }

    public synchronized PiServer.ClientHandler get(SecurityComponent sensor) {
        return map.get(sensor);
    }

    public synchronized void remove(Sensor sensor) {
        map.remove(sensor);
    }

   // public void lista() {
   //     map.forEach((sensor, ch) -> System.out.println(user.getUserName() + " " + user.getImage().getDescription()));
   // }

  //  public synchronized ArrayList<Sensor> getOnlineusers() {
  //      return new ArrayList<User>(clients.keySet());
   // }


    public boolean containsSensor(SecurityComponent sensor) {
        return map.containsKey(sensor);
    }

    public int size() {
        return map.size();
    }

    public void replace(SecurityComponent sensor, PiServer.ClientHandler ch) {
        map.replace(sensor,ch);
    }

    public Iterable<? extends SecurityComponent> keySet() {
        return map.keySet();
    }
}


