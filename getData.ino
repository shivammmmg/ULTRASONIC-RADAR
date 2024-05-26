/*!
 *@file getData.ino
 *@brief Read ambient temperature and relative humidity and print them to serial port.
 *@copyright   Copyright (c) 2010 DFRobot Co.Ltd (http://www.dfrobot.com)
 *@licence     The MIT License (MIT)
 *@author [fengli](li.feng@dfrobot.com)
 *@version  V1.0
 *@date  2021-6-24
 *@get from https://www.dfrobot.com
 *@https://github.com/DFRobot/DFRobot_DHT20
*/

#include <DFRobot_DHT20.h>
/*!
 * @brief Construct the function
 * @param pWire IC bus pointer object and construction device, can both pass or not pass parameters, Wire in default.
 * @param address Chip IIC address, 0x38 in default.
 */
DFRobot_DHT20 dht20;
int counter = 0;
void setup(){

  Serial.begin(9600);
  //Initialize sensor
  while(dht20.begin()){
    Serial.println("Initialize sensor failed");
    delay(1000);
  }
}

void loop(){
  if(counter <= 10)
  {
    //Get ambient temperature
    Serial.print("Temperature:"); 
    Serial.print(dht20.getTemperature());
    Serial.print("\t");
    Serial.print("Humidity: ");
    //Get relative humidity
    Serial.println(dht20.getHumidity()*100);
    //Serial.print("counter is ");
    //Serial.println(counter);
    counter++;
  }
  else {
    Serial.end();
    
  return;
  }
  delay(1500);

}
