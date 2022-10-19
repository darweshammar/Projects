//author Karl Andersson, Jens Moths, Olof Persson
#include <ESP8266WiFi.h>          //https://github.com/esp8266/Arduino

//needed libraries for WifiManager
#include <DNSServer.h>
#include <ESP8266WebServer.h>    //https://github.com/esp8266/Arduino
#include <WiFiManager.h>         //https://github.com/tzapu/WiFiManager
#include <Servo.h>
//String locationString = "location";
int buttonState = 0;         // current state of the button
int lastButtonState = 0;     // previous state of the button
//const String IP = "83.254.129.68"; //per
//const String IP = "192.168.1.42";
const String IP = "109.228.172.110";
const int PORT = 40000;
const String TYPEMAGNET = "magnet";
const String TYPEDOOR = "door";
// defines pins numbers
const int servo = 4;
const int magnetReader = 0;
const int led = 13;
const int wifiReset = 16;
const int resetState = 12;
// defines variables
int ledState = LOW;
unsigned long preMillis = 0;
unsigned long ms;
unsigned long beatMs;
unsigned long preBeat = 0;
unsigned long connectMs;
unsigned long preConnect = 0;
unsigned long reconnectMs;
unsigned long preReconnect = 0;

Servo myservo;  // create servo object to control a servo
WiFiClient door;
WiFiClient magnet;
WiFiManager wifiManager;

String setupWifiManager() {

  //Parameter for configuring the location at the same time as wifi
  WiFiManagerParameter location("location", "location", "", 40);
  wifiManager.addParameter(&location);

  //fetches ssid and pass from eeprom and tries to connect
  //if it does not connect it starts an access point with the specified name
  //and goes into a BLOCKING loop awaiting configuration
  wifiManager.autoConnect("DoorMagnet");

  //if you get here you have connected to the WiFi
  Serial.println("connected to wifi");
  Serial.println(location.getValue());
  return location.getValue();
}
void resetWifi() {
  int wifiButton = digitalRead(wifiReset);
  if (wifiButton == HIGH) {
    wifiManager.resetSettings();
    delay(2000);
    pinMode(resetState, OUTPUT);
  }
}
void ledBlink() {
  ms = millis();
  if ((ms - preMillis) >= 500 ) {
    preMillis = ms;
    if (ledState == LOW) {
      ledState = HIGH;
    } else {
      ledState = LOW;
    } digitalWrite(led, ledState);
  }
}

void heartBeat() {
  beatMs = millis();
  if ((beatMs - preBeat) >= 3000 ) {
    preBeat = beatMs;
    door.println("heartbeat");
    magnet.println("heartbeat");
    Serial.println("heartbeat door");
    Serial.println("heartbeat magnet");
  }
}

void connectToServerDoor(String location) {
  while (true) {
    ledBlink();
    resetWifi();
    connectMs = millis();
    if ((connectMs - preConnect) >= 5000 ) {
      preConnect = connectMs;
      Serial.println("connecting to server door");
      door.connect(IP, PORT);
      door.print(ESP.getChipId() + 1);
      door.print("|");
      door.print(TYPEDOOR);
      door.print("|");
      door.print(location);
      door.println();
    }
    yield();
    if (door.connected()) break;
  }
  Serial.println("connected to server door");
}

void connectToServerMagnet(String location) {
  while (true) {
    ledBlink();
    resetWifi();
    connectMs = millis();
    if ((connectMs - preConnect) >= 5000 ) {
      preConnect = connectMs;
      Serial.println("connecting to server magnet");
      magnet.connect(IP, PORT);
      magnet.print(ESP.getChipId());
      magnet.print("|");
      magnet.print(TYPEMAGNET);
      magnet.print("|");
      magnet.print(location);
      magnet.println();
    }
    yield();
    if (magnet.connected()) break;
  }
  Serial.println("connected to server magnet");
}

void reconnectToServerDoor() {
  while (true) {
    ledBlink();
    resetWifi();
    reconnectMs = millis();
    if ((reconnectMs - preReconnect) >= 5000) {
      preReconnect = reconnectMs;
      Serial.println("reconnecting to server door");
      door.connect(IP, PORT);
      door.print(ESP.getChipId() + 1);
      door.print("|");
      door.println(TYPEDOOR);
    }
    yield();
    if (door.connected()) break;
  }
  Serial.println("connected to server door");
}
void reconnectToServerMagnet() {
  while (true) {
    ledBlink();
    resetWifi();
    reconnectMs = millis();
    if ((reconnectMs - preReconnect) >= 5000) {
      preReconnect = reconnectMs;
      Serial.println("reconnecting to server");
      magnet.connect(IP, PORT);
      magnet.print(ESP.getChipId());
      magnet.print("|");
      magnet.println(TYPEMAGNET);
    }
    yield();
    if (magnet.connected()) break;
  }
  Serial.println("connected to server magnet");
}




void setup() {

  pinMode(magnetReader, INPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(led, OUTPUT);
  myservo.attach(servo);  // attaches the servo on GIO4 to the servo object
  door.setTimeout(250);
  magnet.setTimeout(250);
  //start serial for debugging
  Serial.begin(9600);
  
  //method for easy connection to a wifi
  String location = setupWifiManager();
  
  connectToServerDoor(location);
  connectToServerMagnet(location);
}

void loop() {
  heartBeat();
  resetWifi();
  digitalWrite(led, HIGH);
  if (magnet.connected()) {
    buttonState = digitalRead(magnetReader);
    if (buttonState != lastButtonState) {
      if (buttonState == LOW) {
        magnet.println("off");
        Serial.println("off");
      } else {
        Serial.println("on");
        magnet.println("on");
      }
    }
    lastButtonState = buttonState;
  } else reconnectToServerMagnet();

  if (door.connected()) {
    if (door.available() > 0) {
      char message = door.read();
      Serial.println(message);
      if (message == 'c') {
        myservo.write(90);
      } else if (message == 'o') {
        myservo.write(180);
      }
    }
  } else reconnectToServerDoor();
}
