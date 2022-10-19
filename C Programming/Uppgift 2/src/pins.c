#include "pins.h"
#include "driver/gpio.h"


void initPins()
{
    // sets gpio as output and bit mask to the 12th pin.
    gpio_config_t configPIN;
    configPIN.mode = GPIO_MODE_OUTPUT;
    configPIN.pin_bit_mask = (uint8_t)1<<12;
    gpio_config(&configPIN);
    // sets gpio as output and bit mask to the 14th pin.
    configPIN.pin_bit_mask = (uint8_t)1<<14;
    gpio_config(&configPIN);

    // sets gpio as input, enable pull up and bit mask to the 26th pin.

    gpio_config_t configBUTTON;
    configBUTTON.mode = GPIO_MODE_INPUT;
    configBUTTON.pull_up_en = 1;
    configBUTTON.pin_bit_mask = (uint8_t)1<<26; 
    
    gpio_config(&configBUTTON);

     // sets gpio as input, enable pull up and bit mask to the 27th pin.

    configBUTTON.pin_bit_mask = (uint8_t)1<<27;
    gpio_config(&configBUTTON);

}

// sets the led high if level is 1

void setLEDA(uint8_t level)
{


    if(level)
    {
        gpio_set_level(12,1);

    }else
    {

        gpio_set_level(12,0);

    }

}
