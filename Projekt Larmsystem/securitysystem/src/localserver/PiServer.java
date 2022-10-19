package localserver;

import model.*;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

/**@author Olof Persson, Jens Moths, Per Blomqvist  @coauthor**/
public class PiServer extends Thread implements Serializable {


    private HashMap<String, SecurityComponent> globalMap = new HashMap<>();
    private MicroClients map = new MicroClients();


    private ArrayList<SecurityComponent> fireSensors = new ArrayList<SecurityComponent>();
    private ArrayList<SecurityComponent> magnetSensors = new ArrayList<SecurityComponent>();
    private ArrayList<SecurityComponent> proximitySensors = new ArrayList<SecurityComponent>();
    private ArrayList<SecurityComponent> doorSensors = new ArrayList<SecurityComponent>();
    private ArrayList<SecurityComponent> fingerprintSensor = new ArrayList<>();
    public ArrayList<SecurityComponent> allOnlineSensors = new ArrayList<>();
    public ArrayList<SecurityComponent> allOfflineSensors = new ArrayList<>();
    public GlobalServer globalServer;
    private transient Controller controller;
    public transient StartServer startServer;


    public PiServer(Controller controller) {
        this.controller = controller;
        startServer = new StartServer(controller);

        globalServer = new GlobalServer();


    }

    private void saveKeySet() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("data/keyset.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(map);
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readKeySet() {
        try {
            FileInputStream fileInputStream = new FileInputStream("data/keyset.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            map = (MicroClients) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            // ex.printStackTrace();
        }
    }

    public void setDoor(boolean open) throws IOException {
        for (SecurityComponent s : map.keySet()) {
            if (s instanceof DoorLock) {
                if (open) {
                    map.get(s).sendMessage('o');
                } else {
                    map.get(s).sendMessage('c');
                }
                map.get(s).sensor.setOpen(open);
                s.setOpen(open);
                allOnlineSensors.set(allOnlineSensors.indexOf(s), map.get(s).sensor);
            }
        }
    }

    public void sendToFinger(char c) throws IOException {
        System.out.println("SERVER SEND TO FINGER: "+ c +" Index: ");
        startServer.sendToFingerChar(c);

    }

    public class StartServer extends Thread {

        private SecurityComponent sensor;
        private int port;
        private String location;
        Controller controller;

        StartServer(Controller controller) {
            this.controller = controller;
            port = (Integer.parseInt(JOptionPane.showInputDialog(null, "Välj port")));
            //  this.port = port;
            readKeySet();
            System.out.println(map.size() + " READ SIZE PÅ MAPFILEN PÅ HÅRDDISK");
            start();

        }

        @Override
        public void run() {
            try {

                Socket socket;
                ServerSocket ss = new ServerSocket(port);

                while (true) {
                    socket = ss.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String who = bufferedReader.readLine();

                    System.out.println("CON " + "ID: " + who);

                    String[] split = who.split("\\|");
                    String id = split[0];
                    String type = split[1];
                    if (split.length >= 3 && !split[2].isEmpty())
                    {
                        location = split[2];
                    } else location = "default";

                    switch (type) {
                        case "firealarm":
                            sensor = new FireAlarm(id, location);
                            fireSensors.add(sensor);


                            break;
                        case "magnet":
                            sensor = new MagneticSensor(id, location);
                            magnetSensors.add(sensor);

                            break;

                        case "door":
                            sensor = new DoorLock(id, location);
                            doorSensors.add(sensor);


                            break;
                        case "proximity":
                            sensor = new ProximitySensor(id, location);
                            proximitySensors.add(sensor);
                            break;

                        case "fingerprint":
                            sensor = new FingerprintSensor(id,location);
                            fingerprintSensor.add(sensor);
                            break;

                    }

                    //   globalServer.UpdateGlobal(allOnlineSensors);


                    System.out.println("IP :" + socket.getInetAddress() + " Sensortype: " + sensor.getClass().getSimpleName() + " Location: " + sensor.getLocation() + " SensorID: " + sensor.getId());

                    if (!map.containsSensor(sensor)) {
                        ClientHandler ch = new ClientHandler(socket, sensor);
                        map.put(sensor, ch);
                    } else {
                        String oldLocation = map.get(sensor).sensor.getLocation();
                        System.out.println(oldLocation);
                        if (location.equals("default")) {
                            sensor.setLocation(oldLocation);
                        }

                        map.get(sensor).interrupt();
                        ClientHandler ch = new ClientHandler(socket, sensor);
                        map.replace(sensor, ch);


                        System.out.println("EN RECONNECT HAR SKETT: " + "MK FÅR SIN GAMLA LOCATION: " + sensor.getLocation());
                    }
                    sensor = map.get(sensor).sensor;
                    allOnlineSensors.add(sensor); //TODO NYTT KOPPLA FRÅN SENSOR
                    allOfflineSensors.remove(sensor);
                    globalMap.put(sensor.getId(), sensor); //TODO NYTT
                    //    globalServer.UpdateGlobal(globalMap);
                    System.out.println("MAPSIZE: " + map.size());
                    System.out.println("GLOBALMAPSIZE: " + globalMap.size());


                    controller.updateSensors();
                    saveKeySet();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        public void sendToFingerChar(char msg) throws IOException {
            System.out.println("CH SEND TO FINGER");
            for (SecurityComponent s : map.keySet()
            ) {
                if (s instanceof FingerprintSensor) {
                    map.get(s).sendMessage(msg);
                }

            }
        }
    }


    class ClientHandler extends Thread implements Serializable {
        private transient BufferedReader bufferedReader;
        private transient BufferedWriter bufferedWriter;
        private transient Socket socket;
        private SecurityComponent sensor;
        private long lastRead;
        private int HeartbeatIntervall = 8;


        ClientHandler(Socket socket, SecurityComponent securityComponent) throws IOException {
            this.socket = socket;
            this.sensor = securityComponent;
            //socket.setTcpNoDelay(true); //TODO HA DET HÄR ELLER?
            socket.setSoTimeout(20000);

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            start();
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    String stringMessage = bufferedReader.readLine();

                    if (stringMessage == null) continue;
                    lastRead = System.currentTimeMillis();
                    if (stringMessage.equals("heartbeat")) {
                        continue;
                    }
                    //if (stringMessage.matches(".*\\d.*")) {
                        //set
                      //  continue;
                    //}
                    String[] split = stringMessage.split("\\|");
                    String state = split[0]; //, location = split[1], type = split[2];
                    System.out.println("DETTA HAR SKICKATS FRÅN MK: " + state);
                    boolean booleanState = state.equals("on");


                    sensor.setOpen(booleanState);
                    controller.updateSensors();
                    Message message = new Message("", sensor);


                    if (message.getSecurityComponent() instanceof MagneticSensor) {
                        if (message.getSecurityComponent().isOpen()) {
                            if (Controller.alarmOn) {
                                for (SecurityComponent s : map.keySet()) {
                                    if (s instanceof DoorLock) {
                                        controller.setDoorOpen(false);
                                    }
                                }
                                message.setInfo("Någon har brutit sig in");
                                controller.takePicture();
                                controller.alarmOnDelay("magnet", message);
                            } else {
                                message.setInfo("Någon har öppnat dörren");
                                controller.soundAlarm("greeting");
                                //TODO CONTROLLER SETDOOROPEN
                            }
                        } else {
                            message.setInfo("Någon har stängt dörren");
                        }
                        if (!message.getInfo().equals("Någon har brutit sig in"))
                            globalServer.globalsendMessage(message);
                    }

                    if (message.getSecurityComponent() instanceof ProximitySensor) {
                        if (message.getSecurityComponent().isOpen()) {
                            for (SecurityComponent s : map.keySet()) {
                                if (s instanceof DoorLock) {
                                    if (Controller.alarmOn) {
                                        controller.setDoorOpen(false);
                                    } else controller.setDoorOpen(true);
                                }
                            }
                            if (Controller.alarmOn) {
                                message.setInfo("Rörelse alarm");
                                controller.takePicture();
                                controller.alarmOnDelay("magnet", message);
                            }
                        }
                    }

                    if (message.getSecurityComponent() instanceof FireAlarm) {
                        message.setInfo("Det brinner");
                        globalServer.globalsendMessage(message);
                        controller.soundAlarm("fire");
                        //controller.takePicture();
                        for (SecurityComponent s : map.keySet()) {
                            if (s instanceof DoorLock) {
                                controller.setDoorOpen(true);
                            }
                        }
                    }
                    if (message.getSecurityComponent() instanceof FingerprintSensor) {
                        if (split[0].equals("fingers")) {
                            controller.setFingersAmount(Integer.parseInt(split[1]));
                        }
                        if (message.getSecurityComponent().isOpen()) {
                            controller.setAlarmOn(false);
                            controller.soundAlarm("Welcome");
                            message.setInfo("Fingerläsaren har öppnat dörren ");
                            for (SecurityComponent s : map.keySet()) {
                                if (s instanceof DoorLock) {
                                    controller.setDoorOpen(true);
                                }
                            }
                        } else {
                            if (Controller.alarmOn) {
                                //message.setInfo("Fingerläsaren har larmat");
                                //controller.soundAlarm("Fire");
                            }
                        }
                        globalServer.globalsendMessage(message);
                    }


                    System.out.println("CH " + "IP: " + socket.getInetAddress() + " ID: " + sensor.getId() + " TYPE: " + sensor.getClass().getSimpleName() + " " + message.getSecurityComponent().isOpen());


                } catch (SocketTimeoutException e) {
                    //if ((HeartbeatIntervall > 0) && ((System.currentTimeMillis() - lastRead) > HeartbeatIntervall)) {
                        e.printStackTrace();
                        globalMap.remove(sensor.getId());
                        System.out.println(sensor.getClass().getSimpleName() + " " + socket.getInetAddress() + " Har kopplats bort via HEARTHBEAT YEYEYEYE");
                        break;
                    //}
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            allOnlineSensors.remove(sensor);//TODO NYTT KOPPLA FRÅN SENSOR
            allOfflineSensors.add(sensor);

            controller.updateSensors();
            System.out.println("Borde dö här, breakat while är i slutet av run");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(char msg) throws IOException {
            bufferedWriter.write(msg);
            bufferedWriter.flush();
            System.out.println("DET HÄR: " + msg + " HAR SKICKATS TILL MIKROKONTROLLER");
        }

        public void sendMessageID(int id) throws IOException {
            bufferedWriter.write(id);
            bufferedWriter.flush();
            System.out.println("DET HÄR: " + id + " HAR SKICKATS TILL MIKROKONTROLLER");
        }

    }


    public class GlobalServer implements Runnable, Serializable {
        private transient Socket socket;
        private transient ObjectOutputStream oos;
        private transient ObjectInputStream ois;
        String userName;
        String password;


        public void readUserLoginInfo() {
            try (BufferedReader reader = new BufferedReader(new FileReader("data/userdata.txt"))) {
                userName = reader.readLine();
                password = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void connect(String ip, int port) throws IOException {
            socket = new Socket(ip, port);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            String clientType = "server";

            readUserLoginInfo();
            oos.writeObject(clientType);
            oos.writeObject(userName);
            oos.writeObject(password);
            oos.flush();
            updateGlobal();
        }


        public void globalsendMessage(Message msg) {
            if (socket != null && !socket.isClosed()) {
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());  //FUNGERADE INTE ATT LÄSA OBJEKTETS BOOLEAN OM VI INTE GJORDE NYA STREAMS VARJE GÅNG VI SKICKADE
                    oos.writeObject(msg);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("sent message " + msg.getInfo() + "to GlobalServer: " + socket.toString());
            }
        }

        public void updateGlobal() { //TODO EJ TESTAD METOD
            Message msg = new Message(Controller.alarmOn, allOnlineSensors, allOfflineSensors);

            if (socket != null && !socket.isClosed()) {
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msg);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void shutdownSensor(Message msg) throws IOException {  //TODO EJ TESTAD METOD

            //  map.get(msg.getSecurityComponent()).socket.close();
            map.get(msg.getSecurityComponent()).sendMessage('k');
        }

        public void globalsendPicture(ImageIcon icon) throws IOException {
            //JOptionPane.showMessageDialog(null, icon);
            if (socket != null && !socket.isClosed()) {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(icon);
                oos.flush();
            }
        }

        @Override
        public void run() {
            try {
                connect("109.228.172.110", 8081);
                System.out.println("connected to server");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Kunde inte ansluta till servern \n" + e.getMessage());
                e.printStackTrace();
            }
            while (socket.isConnected()) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        if (msg.getInfo().equals("new location")) {

                            map.get(msg.getSecurityComponent()).sensor.setLocation(msg.getSecurityComponent().getLocation());//TODO Byta hela sensorn?


                            if (allOnlineSensors.contains(msg.getSecurityComponent()))
                                allOnlineSensors.set(allOnlineSensors.indexOf(msg.getSecurityComponent()), msg.getSecurityComponent());
//                            else if (allOfflineSensors.contains(msg.getSecurityComponent()))
//                                allOfflineSensors.set(allOfflineSensors.indexOf(msg.getSecurityComponent()), msg.getSecurityComponent());
                            controller.updateSensors();
                            saveKeySet();
                        }
                        else if (msg.getSecurityComponent() instanceof DoorLock) {
                            for (SecurityComponent s : map.keySet()) {
                                if (s instanceof DoorLock) {
                                    if (msg.getSecurityComponent().isOpen()) {
                                       controller.setDoorOpen(true);
                                    } else controller.setDoorOpen(false);
                                }
                            }  if (allOnlineSensors.contains(msg.getSecurityComponent())) {
                                allOnlineSensors.set(allOnlineSensors.indexOf(msg.getSecurityComponent()), msg.getSecurityComponent());
                                controller.updateSensors();
                            }

                        }
                    }
                    if (obj instanceof String) {
                        System.out.println("String object vi fått från Global: " + obj);
                        if (obj.equals("user authenticated")) controller.setOnlineButton(false);
                        if (obj.equals("Take photo")) controller.takePicture();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                    break;
                }
            }
            controller.setOnlineButton(true);
        }
    }
}




