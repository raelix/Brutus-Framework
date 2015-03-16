// EmonLibrary examples openenergymonitor.org, Licence GNU GPL V3

#include "EmonLib.h"                   // Include Emon Library
EnergyMonitor emon1;                   // Create an instance

void setup()
{  
  Serial.begin(9600);
  
  emon1.current(2, 17);             // Current: input pin, calibration.
}

void loop()
{
  double Irms = (emon1.calcIrms(1480)-0.052); // Calculate Irms only (1480: no. of samples)
  
  Serial.print(Irms*230.0);	       // Apparent power
  Serial.print(" ");
  Serial.println(Irms);		       // Irms
  delay(2000);
}
