#include <stdio.h>
#include <tchar.h>
#include "SerialClass.h"	// Library described above
#include <string>
#include <time.h>
#include <cstdint>


char BASE_ID = 99;

// application reads from the specified serial port and reports the collected data
int _tmain(int argc, _TCHAR* argv[])
{

	printf("Welcome to the serial test app!\n\n");
	Serial* SP = new Serial("\\\\.\\COM9");    // adjust as needed

	if (SP->IsConnected())
		printf("We're connected\n");

	char incomingData[256] = "";			// don't forget to pre-allocate memory
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
							//read in time of measure
							//read in 12 bytes of accel and gyro measures
							break;
						}
					case 4:								//RFID message recived
						{
							//printf("RFID message");
							//read in time of measure
							//read in 5 bytes of RFID tag ID
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
				
				

		Sleep(10);
	}
	return 0;
}
