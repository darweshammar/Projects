//author Karl Andersson, Jens Moths, Olof Persson
#include <ESP8266WiFi.h>          //https://github.com/esp8266/Arduino

//needed libraries for WifiManager
#include <DNSServer.h>
#include <ESP8266WebServer.h>    //https://github.com/esp8266/Arduino
#include <WiFiManager.h>         //https://github.com/tzapu/WiFiManager

//String locationString = "location";
//const String IP = "83.254.129.68"; //per
//const String IP = "82.209.130.123";//jens
const String IP = "109.228.172.110";
//const String IP = "192.168.1.42";//Olof mobil
const int PORT = 40000; //per
const String TYPE = "firealarm";
// defines pins numbers
const int larm = 15;
int larmSignal;
const int led = 13;
int ledState = LOW;
int wifiReset = 16;
int resetState = 12;
// defines variables
unsigned long preMillis = 0;
unsigned long ms;
unsigned long beatMs;
unsigned long preBeat = 0;
unsigned long connectMs;
unsigned long preConnect = 0;
unsigned long reconnectMs;
unsigned long preReconnect = 0;



WiFiClient client;
WiFiManager wifiManager;

String setupWifiManager() {

  //Parameter for configuring the location at the same time as wifi
  WiFiManagerParameter location("location", "location", "", 40);
  wifiManager.addParameter(&location);

  //fetches ssid and pass from eeprom and tries to connect
  //if it does not connect it starts an access point with the specified name
  //and goes into a BLOCKING loop awaiting configuration
  wifiManager.autoConnect("Brandlarm");

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
    client.println("heartbeat");
    Serial.println("heartbeat firealarm");
  }
}

void connectToServer(String location) {
  while (true) {
    ledBlink();
    resetWifi();
    connectMs = millis();
    if ((connectMs - preConnect) >= 5000 ) {
      preConnect = connectMs;
      Serial.println("connecting to server firealarm");
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
  Serial.println("connected to server firealarm");
}

void reconnectToServer() {
  while (true) {
    ledBlink();
    resetWifi();
    reconnectMs = millis();
    if ((reconnectMs - preReconnect) >= 5000) {
      preReconnect = reconnectMs;
      Serial.println("reconnecting to server firealarm");
      client.connect(IP, PORT);
      client.print(ESP.getChipId());
      client.print("|");
      client.println(TYPE);
    }
    yield();
    if (client.connected()) break;
  }
  Serial.println("connected to server firealarm");
}


void setup() {

  //start serial for debugging
  Serial.begin(9600);
  client.setTimeout(250);
  pinMode(larm, INPUT);
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
    larmSignal = digitalRead(larm);
    if (larmSignal == HIGH) {
      client.println("Fire");
      Serial.println("Fire");
      delay(3000);
    }
    if (client.available() > 0) {
      char message = client.read();
      Serial.println(message);
    }
  } else reconnectToServer();
}
