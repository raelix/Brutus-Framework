#include <Ethernet.h>
#include <SPI.h>
#include <Mudbus.h>
#include <EmonLib.h>

EnergyMonitor emon1; 
Mudbus Mb;

void setup(){
   emon1.current(2, 28.0); 
byte mac[] = { 0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x02 };
IPAddress ip(192,168,0, 11);
IPAddress gateway(192,168,0, 1);
IPAddress subnet(255, 255, 255, 0);
 Ethernet.begin(mac, ip, gateway, subnet);
 
}

void loop(){
 Mb.Run();
 double Irms = emon1.calcIrms(480);
 Mb.R[0] =  (Irms * 230 * 1.015); 
 Mb.R[1] =  Irms * 100;

}
