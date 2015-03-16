#include <SPI.h>
#include <Ethernet.h>
#include <aREST.h> //https://github.com/marcoschwartz/aREST
#include <avr/wdt.h>
#include <EmonLib.h>//https://github.com/openenergymonitor/EmonLib

byte mac[] = { 0x90, 0xA2, 0xDA, 0x0E, 0xFE, 0x40 };
IPAddress ip(192,168,0, 11);
EthernetServer server(80);
aREST rest = aREST();
EnergyMonitor emon1;
float irms = 0;
int powerData = 0;

void setup(void)
{  
  emon1.current(2, 29.0); //emon1.current(2, 28.0); 
  pinMode(5, OUTPUT);    
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(8, OUTPUT);
  digitalWrite(5, HIGH);
  digitalWrite(6, HIGH); 
  digitalWrite(7, HIGH); 
  digitalWrite(8, HIGH); 
  rest.variable("power", &powerData);
  rest.set_id("001");
  rest.set_name("energy_meter");
  Ethernet.begin(mac, ip);
  server.begin();
  wdt_enable(WDTO_4S);
}

void loop() {  
  EthernetClient client = server.available();
  rest.handle(client);
  wdt_reset();
  irms = emon1.calcIrms(480) * 230 * 1.015;//emon1.calcIrms(1480) * 223 * 1.015
  powerData = irms;
}


