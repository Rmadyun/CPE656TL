#include <Wire.h>
#include "config.h"
#include "measurementRelay.h"

measurementRelay mR;

void setup() {
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);
  
  Serial.begin(9600); //Might look at increasing this, but will need to be changed in XBees as well

  mR.init();


}

void loop() {
  mR.listenForMeasurements();
}
