#ifndef MEASUREMENTRELAY_H
#define MEASUREMENTRELAY_H


#include "config.h"


class measurementRelay
{
  unsigned long timeOfDay;
  unsigned long millisOffset;

  public:
    void init();
    void listenForMeasurements();
  private:
    void baseLink();
    unsigned long measureRTT();
    void timeLink(unsigned long RTT);
};

//**************************************************************************
//
//  Initialize by linking to base, determining RTT, and getting time
//
//**************************************************************************

void measurementRelay::init()
{

  baseLink();                         //Used to announce MDU to base computer

  unsigned long cRTT = measureRTT();  //Callculate RTT to base computer

  timeLink(cRTT);                     //Query time of day from base computer
}



//*********************************************************************************
//
//  Listen for RFID measurements over serial and IMU readings either from 
//    interrupts or by request (selectable)
//
//*********************************************************************************

void measurementRelay::listenForMeasurements()
{
  byte i = 0;
  byte val = 0;
  byte code[6];
  byte checksum = 0;
  byte bytesread = 0;
  byte tempbyte = 0;

   if(Serial.available() > 0)
   {
      if(val = Serial.read() == 2)                //read a message from the RFID reader
      {
          bytesread = 0;
          while (bytesread < 12) {                        // read 10 digit code + 2 digit checksum
            if( Serial.available() > 0) { 
              val = Serial.read();
              if((val == 0x0D)||(val == 0x0A)||(val == 0x03)||(val == 0x02)) { // if header or stop bytes before the 10 digit reading 
                break;                                    // stop reading
              }
    
              // Do Ascii/Hex conversion:
              if ((val >= '0') && (val <= '9')) {
                val = val - '0';
              } else if ((val >= 'A') && (val <= 'F')) {
                val = 10 + val - 'A';
              }
    
              // Every two hex-digits, add byte to code:
              if (bytesread & 1 == 1) {
                // make some space for this hex-digit by
                // shifting the previous hex-digit with 4 bits to the left:
                code[bytesread >> 1] = (val | (tempbyte << 4));
                
              } else {
                tempbyte = val;                           // Store the first hex digit first...
              };
    
              bytesread++;                                // ready to read next digit
            } 
          }
    
          if (bytesread == 12) {                          // if 12 digit read is complete
            
            byte Rmsg [13];
            Rmsg[0] = BASE_ID;
            Rmsg[1] = TRAIN_ID;
            Rmsg[2] = RFID;

            //test block till time implementation**********************************************************
            Rmsg[3] = 0x3;
            Rmsg[4] = 0x4;
            Rmsg[5] = 0x5;
            Rmsg[6] = 0x6;
            //test block till time implementation*********************************************************

            for (i=0; i<5; i++) {
              Rmsg[7+i] = code[i];
            }
            Rmsg[12] = '\n';

            Serial.write(Rmsg, 13);
          }
    
          bytesread = 0;
      }     //end RFID reader message parser
    
   }



  //************************************************************************************************
  // Use section below to operate linearly and not use interrupts
  //************************************************************************************************

  //Serial.println(BASE_ID + TRAIN_ID + IMU + millis() + AcX + AcY + AcZ + GyX + GyY + GyZ)
 
  int16_t Tmp;
  unsigned long tme;
  
   
  byte msg [20];
  
  //debug loop
  for(int i = 0; i < 20; i++)
  {
    msg[i] = i;
  }
  //*********

  msg[0] = BASE_ID;
  msg[1] = TRAIN_ID;
  msg[2] = IMU;

  tme = timeOfDay + (millis() - millisOffset);
  msg[3] = 0x03; //(int)((tme >> 24) & 0xFF) ;
  msg[4] = 0x04; //(int)((tme >> 16) & 0xFF) ;
  msg[5] = 0x05; //(int)((tme >> 8) & 0XFF);
  msg[6] = 0x06; //(int)((tme & 0XFF));
  
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(MPU_addr,14,true);  // request a total of 14 registers
  
  msg[7] = Wire.read(); msg[8] = Wire.read();     //AcX
  msg[9] = Wire.read(); msg[10] = Wire.read();   //AcY
  msg[11] = Wire.read(); msg[12] = Wire.read();   //AcZ
  
  Tmp=Wire.read()<<8|Wire.read();  // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
  
  msg[13] = Wire.read(); msg[14] = Wire.read();     //GyX
  msg[15] = Wire.read(); msg[16] = Wire.read();   //GyY
  msg[17] = Wire.read(); msg[18] = Wire.read();   //GyZ
  msg[19] = '\n';

  Serial.print(Serial.write(msg, 20));
  //Serial.write(msg, 20);
}


//*******************************************************************************
//
//  Send the ID message and completes the link procedure with the base computer
//
//*******************************************************************************

void measurementRelay::baseLink()
{
  unsigned long timeout;
  bool linkstatus = false;
  int toDur = 1500;                                       //timeout duration in milliseconds
  byte val = 0;

  byte IDmsg [4] = {BASE_ID, TRAIN_ID, ID, '\n'};         //Build ID message
    
  while(!linkstatus)                                      //wait for ID_RESP message
  {
    Serial.write(IDmsg, 4);                                 //Send ID message
    timeout = millis();
    while((millis() - timeout) < toDur)                      //set timeout to 1.5 seconds
    {
      if(val = Serial.available() > 0)
      {
        byte head[2];

        if(val = Serial.read() == TRAIN_ID)
        {
          Serial.readBytes(head, 2);
          if(head[1] == ID_RESP)
            {
              linkstatus = true;
            }
        }
      }   
    } 
  }
}


//********************************************************************************
//
//  Measures the RTT to the base computer
//
//********************************************************************************

unsigned long measurementRelay::measureRTT()
{
  unsigned long startTime, endTime;
  unsigned long latency = 0;
  int measures = 0;
  byte RTTmsg[4] = {BASE_ID, TRAIN_ID, RTT_REQ, '\n'};    //Build RTT_REQ
  bool rtttest = false;
  while(!rtttest)
  {
    startTime = millis();
    Serial.write(RTTmsg, 4);
    while(millis() - startTime < 1500)
    {
      if(Serial.available())
      {
        byte head[2];
        if(Serial.read() == TRAIN_ID)
        {
          Serial.readBytes(head, 2);
          if(head[1] == RTT_RESP)
          {
            latency = millis() - startTime;
              rtttest = true;                    
          }
        }
      } 
    }
    
  }

  return latency;
}


//***********************************************************************************
//
//  Link to the base computer's time of day. 
//    Inputs: RTT of comunication link in milliseconds
//
//***********************************************************************************

void measurementRelay::timeLink(unsigned long RTT)
{
   byte TMEmsg [4] = {BASE_ID, TRAIN_ID, TIME, '\n'};    //Request time
  
  bool ttest = false;
  unsigned long regTime, endTime, startTime;
  while(ttest == false)
    {
      Serial.write(TMEmsg, 4);
      startTime = millis();
      while(millis() - startTime < 1500)                              //Wait for serial
      {
        if(Serial.available())
        {
          byte head[2];
          if(Serial.read() == TRAIN_ID)
          {
            Serial.readBytes(head, 2);
            if(head[1] == TIME_RESP)                     //Recieve TIME_RESP
            {
              endTime = millis();
              byte timeBuff[4];
              Serial.readBytes(timeBuff, 4);
              //regTime = timeBuff[0]<<24|timeBuff[1]<<16|timeBuff[2]<<8|timeBuff[3];
              ttest = true;
            }
          }
        }
      }
      
    }
  
  timeOfDay = regTime * 1000; //calculate ofset value for millis()
  millisOffset = startTime + (endTime - startTime)/2;
}



#endif

