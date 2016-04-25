#ifndef MEASUREMENTRELAY_H
#define MEASUREMENTRELAY_H


#include "imuDriver.h"
#include "config.h"


class measurementRelay
{
  unsigned long timeOfDay;
  unsigned long millisOffset;
  imuDriver MDU;

  public:
    void init();
    void forwardIMU();
    void listenForMeasurements();
    void linkIMU(imuDriver * imu);
};

void measurementRelay::init()
{

  Serial.println("test");

  
  byte IDmsg [4] = {BASE_ID, TRAIN_ID, ID, '\n'};         //Build ID message
  Serial.write(IDmsg, 4);                                 //Send ID message

  unsigned long startTime, endTime;
  unsigned long latency = 0;
  byte RTTmsg[4] = {BASE_ID, TRAIN_ID, RTT_REQ, '\n'};    //Build RTT_REQ
  bool rtttest = false;
  for( int i = 0; i < 3; i++)                             //Send an RTT_REQ message 3 times
  {
    Serial.write(RTTmsg, 4);
    startTime = millis();
    while(rtttest == false)
    {
      while(!Serial){}
      if(Serial.read() == TRAIN_ID)
      {
        Serial.read();
        if(Serial.read() == RTT_RESP)                     //Recieve RTT_RESP
        {
          latency += (millis() - startTime);
          rtttest = true;
        }
      }
      
    }
    rtttest = false;
  }

  latency = latency/3;

  byte TMEmsg [4] = {BASE_ID, TRAIN_ID, TIME, '\n'};    //Request time
  
  startTime = millis();
  Serial.write(TMEmsg, 4);
  bool ttest = false;
  unsigned long regTime;
  while(ttest == false)
    {
      while(!Serial){}                              //Wait for serial
      if(Serial.read() == TRAIN_ID)
      {
        Serial.read();
        if(Serial.read() == TIME_RESP)                     //Recieve TIME_RESP
        {
          endTime = millis();
          regTime = Serial.read()<<24|Serial.read()<<16|Serial.read()<<8|Serial.read();
          ttest = true;
        }
      }
      
    }
  
  timeOfDay = regTime * 1000; //calculate ofset value for millis()
  millisOffset = startTime + (endTime - startTime)/2;
  
}




void measurementRelay::forwardIMU()
{
  
}





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
            //test block till timne implementation*********************************************************

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
  // 
  // Use section below to operate linearly and not use interrupts
  //
  //************************************************************************************************

  //Serial.println(BASE_ID + TRAIN_ID + IMU + millis() + AcX + AcY + AcZ + GyX + GyY + GyZ)
 
  int16_t Tmp;
  unsigned long tme;
  
  byte msg [20];
  msg[0] = BASE_ID;
  msg[1] = TRAIN_ID;
  msg[2] = IMU;

  tme = timeOfDay + (millis() - millisOffset);
  msg[4] = 0x04; //(int)((tme >> 24) & 0xFF) ;
  msg[5] = 0x05; //(int)((tme >> 16) & 0xFF) ;
  msg[6] = 0x06; //(int)((tme >> 8) & 0XFF);
  msg[7] = 0x07; //(int)((tme & 0XFF));
  
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(MPU_addr,14,true);  // request a total of 14 registers
  
  msg[8] = Wire.read(); msg[9] = Wire.read();     //AcX
  msg[10] = Wire.read(); msg[11] = Wire.read();   //AcY
  msg[12] = Wire.read(); msg[13] = Wire.read();   //AcZ
  
  Tmp=Wire.read()<<8|Wire.read();  // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
  
  msg[14] = Wire.read(); msg[15] = Wire.read();     //GyX
  msg[16] = Wire.read(); msg[17] = Wire.read();   //GyY
  msg[18] = Wire.read(); msg[19] = Wire.read();   //GyZ
  msg[20] = '\n';

  Serial.write(msg, 20);
}





void measurementRelay::linkIMU(imuDriver * imu)
{
  
}


#endif

