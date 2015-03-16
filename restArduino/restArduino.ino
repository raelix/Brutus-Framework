#include <SPI.h>
#include <Ethernet.h>
#include <aREST.h>
#include <avr/wdt.h>
#include <EmonLib.h>
// Enter a MAC address for your controller below.
byte mac[] = { 0x90, 0xA2, 0xDA, 0x0E, 0xFE, 0x40 };

// IP address in case DHCP fails
IPAddress ip(192,168,0, 11);

// Ethernet server
EthernetServer server(80);

// Create aREST instance
aREST rest = aREST();
EnergyMonitor emon1;
// Variables to be exposed to the API
int irms = 0;

void setup(void)
{  
  //Serial.begin(115200);
  
  emon1.current(2, 28.0); 
  rest.variable("power", &irms);
  //rest.function("led",ledControl);
  rest.set_id("001");
  rest.set_name("energy_meter");

  // Start the Ethernet connection and the server
 // if (Ethernet.begin(mac) == 0) {
 //   Serial.println("Failed to configure Ethernet using DHCP");
    Ethernet.begin(mac, ip);
 // }
  server.begin();
  //Serial.print("server is at ");
 // Serial.println(Ethernet.localIP());
  // Start watchdog
  wdt_enable(WDTO_4S);
}

void loop() {  
  
  // listen for incoming clients
  EthernetClient client = server.available();
  rest.handle(client);
  wdt_reset();
  irms = emon1.calcIrms(480) * 230 * 1.015;
}

// Custom function accessible by the API
//int ledControl(String command) {
  // Get state from command
 // int state = command.toInt();
 // digitalWrite(4,state);
//  return digitalRead(4);
//}

