/*
    Mudbus.h - an Arduino library for a Modbus TCP slave.
    Copyright (C) 2011  Dee Wykoff

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

//#define MbDebug

// For Arduino 0022
// #include "WProgram.h"
// For Arduino 1.0
#include "Arduino.h"

#include <SPI.h>
#include <Ethernet.h>

#ifndef Mudbus_h
#define Mudbus_h

#define MB_N_R 125 //Max 16 bit registers for Modbus is 125
#define MB_N_C 128 //Max coils for Modbus is 2000 - dont need that many so here is a multiple of 8
#define MB_PORT 502

enum MB_FC {
  MB_FC_NONE           = 0,
  MB_FC_READ_COILS     = 1,
  MB_FC_READ_REGISTERS = 3,
  MB_FC_WRITE_COIL     = 5,
  MB_FC_WRITE_REGISTER = 6,
  MB_FC_WRITE_MULTIPLE_COILS = 15,
  MB_FC_WRITE_MULTIPLE_REGISTERS = 16
};

class Mudbus{
public:
  Mudbus();
  void Run();  
  int  R[MB_N_R];
  bool C[MB_N_C];  
  bool Active;    
  unsigned long PreviousActivityTime;
  int Runs, Reads, Writes;
private: 
  uint8_t ByteArray[260];
  MB_FC FC;
  void SetFC(int fc);
};
#endif

