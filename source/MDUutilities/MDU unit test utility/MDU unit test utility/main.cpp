#include <stdio.h>
#include <tchar.h>
#include "SerialClass.h"
#include <string>
#include <time.h>
#include <cstdint>
#include <iostream>


using namespace std;

char BASE_ID = 99;

// application reads from the specified serial port and reports the collected data
int _tmain(int argc, _TCHAR* argv[])
{
	bool iOut = false;
	bool rOut = false;
	printf("MDU Unit Test Utility\n\n");
	printf("Enter Port Number: ");
	char pnum = 0;
	cin >> pnum;
	char comID[11] = { '\\', '\\', '.', '\\', 'C', 'O', 'M', pnum};
	char tst[] = {"\\\\.\\COM9"};

	Serial* SP = new Serial(comID);    // adjust as needed

	if (SP->IsConnected())
		printf("Serial connected\n");

	printf("1 - All output, 2 - IMU only, 3 - RFID only\n");
	int p;
	cin >> p;
	switch (p)
	{
		case 1:
			iOut = true;
			rOut = true;
			break;
		case 2:
			iOut = true;
			break;
		case 3:
			rOut = true;
			break;
	}

	char incomingData[256] = "";			//pre-allocate memory
	//printf("%s\n",incomingData);
	int dataLength = 255;
	int readResult = 0;

	while(SP->IsConnected())
	{
		readResult = SP->ReadData(incomingData,dataLength);
		// printf("Bytes read: (0 means no data available) %i\n",readResult);
                incomingData[readResult] = 0;

				char source = 0;

				if(incomingData[0] == BASE_ID)			//mesasage for base computer
				{
					source = incomingData[1];
					switch (incomingData[2])
					{
					case 3:								//IMU message recived
						{
							
							//printf("IMU Message\n");

							char IMUmessage [20];

							for(int i = 0; i < 20; i++)
							{
								IMUmessage[i] = incomingData[i];
							}

							unsigned long readTime = (unsigned long) IMUmessage[3] << 24
													| (unsigned long) IMUmessage[4] << 16
													| (unsigned long) IMUmessage[5] << 8
													| (unsigned long) IMUmessage[6];

							int ax, ay, az, gx, gy, gz;

							ax = (int) IMUmessage[7] << 8
								| (int) IMUmessage[8];
							ay = (int) IMUmessage[9] << 8
								| (int) IMUmessage[10];
							az = (int) IMUmessage[11] << 8
								| (int) IMUmessage[12];

							gx = (int) IMUmessage[13] << 8
								| (int) IMUmessage[14];
							gy = (int) IMUmessage[15] << 8
								| (int) IMUmessage[16];
							gz = (int) IMUmessage[17] << 8
								| (int) IMUmessage[18];

							if(iOut)
								printf("%i, %lu, (%i, %i, %i), (%i, %i, %i)\n", source, readTime, ax, ay, az, gx, gy, gz);


							break;
						}
					case 4:								//RFID message recived
						{
							//printf("RFID message");
							char RFIDmessage [13];

							for(int i = 0; i < 13; i++)
							{
								RFIDmessage[i] = incomingData[i];
							}

							unsigned long readTime = (unsigned long) RFIDmessage[3] << 24
													| (unsigned long) RFIDmessage[4] << 16
													| (unsigned long) RFIDmessage[5] << 8
													| (unsigned long) RFIDmessage[6];
							
							if(rOut)
								printf("%i, %lu, %#x, %#x, %#x, %#x, %#x\n", source, readTime, RFIDmessage[7], RFIDmessage[8], RFIDmessage[9], RFIDmessage[10], RFIDmessage[11]);
							break;
						}
					case 5:								//RTT_REQ message recived
						{
							//printf("RTT_REQ message");
							char idResp[] = {source, BASE_ID, 6, '\n'};
							SP->WriteData(idResp, 4);
							break;
						}
					case 7:								//ID message recived
						{
							//printf("ID message");
							char idResp[] = {source, BASE_ID, 10, '\n'};
							SP->WriteData(idResp, 4);
							break;
						}
					case 8:								//TIME message recived
						{
							//printf("time message");
							time_t timer;
							struct tm * tstruct = new tm;

							time(&timer);
							localtime_s(tstruct, &timer);
							//printf("time: %i, %i, %i\n", tstruct->tm_hour, tstruct->tm_min, tstruct->tm_sec);
							uint32_t seconds = ((tstruct->tm_hour * 60) + tstruct->tm_min) * 60 + tstruct->tm_sec;

							char timeResp[8] = {source, BASE_ID, 9, 3,4,5,6, '\n'};
							
							for(int i = 3; i < 7; i++)
							{
								timeResp[i] = seconds >> 8*(i-3);
							}

							SP->WriteData(timeResp, 8);
							break;
						}
					default:
						break;
					}

					/*printf("%s\n", incomingData);		//display block for incoming messages as both plain test and ints
					for(int i=0; i<readResult; i++)
							{
								printf("%10i", incomingData[i]);
							}
					printf("\n");*/

				}
				
				

		//Sleep(10);
	}
	return 0;
}
