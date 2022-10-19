//author Karl Andersson, Jens Moths, Malek Abdul Sater
#include <ESP8266WiFi.h>          //https://github.com/esp8266/Arduino

//needed libraries for WifiManager
#include <DNSServer.h>
#include <ESP8266WebServer.h>    //https://github.com/esp8266/Arduino
#include <WiFiManager.h>         //https://github.com/tzapu/WiFiManager
#include <Ultrasonic.h>          //www.elecfreaks.com

//String locationString = "location";


//const String IP = "83.254.129.68";
//const String IP = "192.168.1.42";//Olof mobil
const String IP = "109.228.172.110";
const int PORT = 40000;
const String TYPE = "proximity";
// defines pins numbers
const int trigpin = 4;
const int echopin = 5;
const int wifiReset = 16;
const int led = 13;
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
float lastDistance = 0;  // current state of the distance

WiFiClient client;
WiFiManager wifiManager;
Ultrasonic ultrasonic(trigpin, echopin);

String setupWifiManager() {

  //Parameter for configuring the location at the same time as wifi
  WiFiManagerParameter location("location", "location", "", 40);
  wifiManager.addParameter(&location);

  //fetches ssid and pass from eeprom and tries to connect
  //if it does not connect it starts an access point with the specified name
  //and goes into a BLOCKING loop awaiting configuration
  wifiManager.autoConnect("Proximity");

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
  if ((beatMs - preBeat) >= 1000 ) {
    preBeat = beatMs;
    client.println("heartbeat");
    Serial.println("heartbeat Proxi");
  }
}

void connectToServer(String location) {
  while (true) {
    ledBlink();
    resetWifi();
    connectMs = millis();
    if ((connectMs - preConnect) >= 5000 ) {
      preConnect = connectMs;
      Serial.println("connecting to server");
      client.connect(IP, PORT);
      client.print(ESP.getChipId());
      client.print("|");
      client.print(TYPE);
      client.print("|");
      client.print(location);
      client.println();
    }
    yield();
    if (client.connected()) break;
  }
  Serial.println("connected to server");
}

void reconnectToServer() {
  while (true) {
    ledBlink();
    resetWifi();
    reconnectMs = millis();
    if ((reconnectMs - preReconnect) >= 5000) {
      preReconnect = reconnectMs;
      Serial.println("reconnecting to server");
      client.connect(IP, PORT);
      client.print(ESP.getChipId());
      client.print("|");
      client.println(TYPE);
    }
    yield();
    if (client.connected()) break;

  }
  Serial.println("connected to server");
}

void setup() {
  //start serial for debugging
  Serial.begin(9600);
  client.setTimeout(250);
  pinMode(led, OUTPUT);
  pinMode(wifiReset, INPUT);
  pinMode(resetState, INPUT);
  digitalWrite(resetState, LOW);

  //method for easy connection to a wifi
  String location = setupWifiManager();

  connectToServer(location);
}

void loop() {
  if (client.connected()) {
    resetWifi();
    heartBeat();
    digitalWrite(led, HIGH);
    float cmdistance, indistance;
    long microsec = ultrasonic.timing();
    cmdistance = ultrasonic.CalcDistance(microsec, Ultrasonic::CM); //this result unit is centimeter

    delay(10);
    
    if (((lastDistance - cmdistance) > 10) || ((lastDistance - cmdistance) <  -10  )) {
      if (cmdistance < 20.00) {

        ms = millis();
        if ((ms - preMillis) >= 5000 ) {
          preMillis = ms;
          client.println("on");
          Serial.print("Distance: ");
          Serial.println(cmdistance);
          lastDistance = cmdistance;
        }
      }
    }
  } else reconnectToServer();
}
