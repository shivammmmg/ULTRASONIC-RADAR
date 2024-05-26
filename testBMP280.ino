/**************************************************************************
   Tests the getPressure functions
 **************************************************************************/
#include <BMP280.h>
BMP280 bmp280;
int counter = 0;
void setup()
{
  Serial.begin(9600);
  delay(40);
  
  Wire.begin(); //Join I2C bus
  bmp280.begin();
}

void loop()
{
  
  //Get pressure value
  uint32_t pressure = bmp280.getPressure();
  float temperature = bmp280.getTemperature();

  //Print the results
  Serial.print("Temperature: ");
  Serial.print(temperature);
  Serial.print("\t");
  Serial.print("Pressure: ");
  Serial.println(pressure);
  counter++;
  delay(2000);
  if(counter > 10)
  {
    Serial.end();
    return;
      }
}
