#include "random.h"


// method for random seconds.
int getRandomFrequency(int min, int max){

  float random = (float) esp_random()/(float)UINT32_MAX;

  return (int) (min + random * (max-min));

}