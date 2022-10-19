#include <stdio.h>
#include <esp_task_wdt.h>
#include "sound_gen.h"
#include "sampler.h"
#include "pins.h"
#include "random.h"

// blinks an LED

void blinkLED(int ms, int times)
{
    for (int i = 0; i < times; i++)
    {
        setLEDA(1);
        vTaskDelay(pdMS_TO_TICKS(ms));
        setLEDA(0);
        vTaskDelay(pdMS_TO_TICKS(ms));
    }
}
// absolute value of a float
float absval(float a)
{
    if (a < 0)
        return -a;
    else
        return a;
}

void app_main()
{
    initPins();
    while (1)
    {

        printf("Starting the game!\n");
        // signal the start of the game, very fast blinking
        blinkLED(50, 20);
        // choose random frequency
        int ranFreq = getRandomFrequency(200, 1000);
        // start sound
        int actualFreq = startSound(ranFreq);
        // wait
        vTaskDelay(pdMS_TO_TICKS(3000));
        // stop the sound
        stopSound();
        // start sampling
        startSampling(4000);
        // wait
        vTaskDelay(pdMS_TO_TICKS(1000));
        // stop sampling
        stopSampling();
        // get frequency
        float playerFreq = getFrequency();

        printf("player: %.2f, actual: %d\n", playerFreq, actualFreq);

        if (absval(playerFreq - (float)actualFreq) < 50)
        {
            printf("Player wins!\n");
            // blink fast for 2 secs
            blinkLED(100, 20);
        }
        else
        {
            printf("Player loses!\n");
            // blink slow for 2 secs
            blinkLED(1000, 2);
        }
    }
}