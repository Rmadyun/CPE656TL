#ifndef CONFIG_H
#define CONFIG_H


byte TRAIN_ID = 0x1a;  //This is ID used in messages. Should match the ID of the train in the Loconet commands for clarity. 2 is reserved.
byte BASE_ID = 0x63;    //The ID for the base computer. Should verify that setting this to 0 is not an issue.

enum mID: byte { IMU=3, RFID=4, RTT_REQ=5, RTT_RESP=6, ID = 7, TIME=8, TIME_RESP = 9 };

const int MPU_addr=0x68;  // I2C address of the MPU-6050

#endif
