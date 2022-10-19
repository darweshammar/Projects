#include <esp_task_wdt.h>
#include "pins.h"
#include "random.h"

/*
 Waits for "millis" milliseconds without upsetting the watchdog timer
*/
void waitMs(unsigned int millis)
{
 TickType_t delay = millis / portTICK_PERIOD_MS;
 vTaskDelay(delay);
}

//code that uses "pinA" argument as a level and then flashes the LED, the argument pinA is either 1 or 0, ms stands for milliseconds.
void flashPinA(uint8_t pinA, unsigned int ms)
{

    setLEDA(pinA);
    waitMs(ms);
    setLEDA(!pinA);
    waitMs(ms);
    setLEDA(pinA);
    waitMs(ms);
    setLEDB(!pinA);
    waitMs(ms);
    

}
//code that uses "pinB" argument as a level and then flashes the LED, the argument pinA is either 1 or 0, ms stands for milliseconds.

void flashPinB(uint8_t pinB, unsigned int ms)
{

    setLEDB(pinB);
    waitMs(ms);
    setLEDB(!pinB);
    waitMs(ms);
    setLEDB(pinB);
    waitMs(ms);
    setLEDB(!pinB);
    waitMs(ms);
    

}


void app_main()
{
 initPins();
    

 while (1)
 {
    //flashing to start the game 
    setLEDA(0);
    setLEDB(1);
    waitMs(500);
    
    setLEDA(1);
    setLEDB(0);
    waitMs(500);
   
    setLEDA(0);
    setLEDB(1);
    waitMs(500);


    setLEDA(1);
    setLEDB(1);

   // waits random seconds to turn off the lights
   int random = getRandomSecs(1000,5000);
   printf("random value: %d\n", random);
   waitMs(random);
   // turn off lights
   setLEDA(0);
   setLEDB(0);


 uint8_t winner = 0;

 // while winner ==0 check if any button is pressed if a button is pressed give winner a value eihter 1 or 2.
 while (!winner)
 {

    isButtonAPressed();

   if(isButtonAPressed()){

         winner = 1;

   }else if(isButtonBPressed()){

         winner = 2;

   }
   

 }

// flash led  depending on the winner
      if (winner == 1)
      {

         setLEDB(0);
         flashPinA(1, 400);
         
      }else if(winner == 2){
         
         setLEDA(0);
         flashPinB(1, 400);

      }
      

 // switch off both A and B and wait for some time to restart the game

   setLEDA(0);
   setLEDB(0);

   waitMs(3000);

 }

 
}
