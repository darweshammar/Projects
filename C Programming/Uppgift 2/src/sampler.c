#include <stdio.h>
#include "sampler.h"
#include "circular_buffer.h"
#include <driver/gpio.h>
#include <driver/adc.h> 
#include <driver/timer.h>
#include <esp32/rom/ets_sys.h> 

#define BUTTON_PIN 26
#define ESP_INTR_FLAG_DEFAULT 0

struct circularBuffer buffer;
int *buffer_data;
static volatile int counter = 0;

void IRAM_ATTR timerISR()
{
    // Note, the following 2 instructions should be called at the beginning of the ISR !
    /* 1: read what caused the interrupt
 interrupts may be grouped, therefore it is possible that another timer
 may have cause the interrupt. Check that it timer 0 the one that caused
 the interrupt.
 Notice that we are accessing registers directly because we do not have
access to the ESP-IDF here.
 */
    uint32_t is_t0 = TIMERG0.int_st_timers.t0;
    if (is_t0)
    {
        /* 2: "Clear" the interrupt.
        Generally, you clear it when you enter the routine,
        indicating that you are going to handle whatever is causing the
        interrupt from that moment in time forward. If another interrupt event occurs
        during
        your servicing of the initial interrupt you don't want to lose that
        interrupt notification. If you clear it after you have serviced the
        interrupt
        and another interrupt event occured in the mean time, you would
        lose that interrupt notification.
        To do that, set the TIMERGN.int_clr_timers.tM structure
        */

        TIMERG0.int_clr_timers.t0 = 1;
        // serve the interrupt: just count up here, but more complex things can happen

        addElement(&buffer, adc1_get_raw(ADC1_CHANNEL_6));

        /* Re-enable the alarm:
        After the alarm has been triggered we need enable it again,
        so it is triggered the next time.
        */
        TIMERG0.hw_timer[0]
        .config.alarm_en = TIMER_ALARM_EN;
    }
}

/*
 Waits for "millis" milliseconds without upsetting the watchdog timer
*/

void startSampling(int freq)
{
    buffer_data = (int *)malloc(freq * sizeof(int));
    initCircularBuffer(&buffer, buffer_data, freq);

    timer_config_t timerConfig;
    // auto-reload the timer = resets the timer when the counter is reached
    timerConfig.auto_reload = TIMER_AUTORELOAD_EN;
    // enable the alarm
    timerConfig.alarm_en = TIMER_ALARM_EN;
    // timer is stopped
    timerConfig.counter_en = TIMER_PAUSE;
    // timer counts up
    timerConfig.counter_dir = TIMER_COUNT_UP;
    // divisor of the incoming 80 MHz APB_CLK clock
    // max is 65536

    timerConfig.divider = (APB_CLK_FREQ / freq); // expected freq = 2KHz
    // timer interrupt level
    timerConfig.intr_type = TIMER_INTR_LEVEL;
    timer_init(TIMER_GROUP_0, TIMER_0, &timerConfig);
    // set the initial value of the counter to 0
    timer_set_counter_value(TIMER_GROUP_0, TIMER_0, 0x00000000ULL);
    // using a counter of 2000, the alarm should be raised at 1 Hz
    timer_set_alarm_value(TIMER_GROUP_0, TIMER_0, 1);
    // enable interrupts
    timer_enable_intr(TIMER_GROUP_0, TIMER_0);
    // register the ISR
    timer_isr_register(TIMER_GROUP_0, TIMER_0, timerISR,
                       NULL, ESP_INTR_FLAG_IRAM, NULL);
    // in flags, you can use ESP_INTR_FLAG_IRAM if you want to use on-chip RAM
    // that's faster, but, you will lose access to the rest of the ESP-IDF
    // you can also pass arguments to the ISR, but we are not doing it here
    // start!
    timer_start(TIMER_GROUP_0, TIMER_0);

    // ADC1 supports GPIOs 32 - 39, let's choose 32.
    // GPIOs - ADC channels mapping is done with macros ADC1_GPION_CHANNEL, so in our case ADC1_GPIO32_CHANNEL
    // start ADC
    adc_power_on();
    // configure pin
    adc_gpio_init(ADC_UNIT_1, ADC1_CHANNEL_6);
    // use full 12 bits width
    adc1_config_width(ADC_WIDTH_BIT_12);
    // set attenuation to support full scale voltage
    adc1_config_channel_atten(ADC1_CHANNEL_6, ADC_ATTEN_DB_11);
    // let's setup a pull-up on that pin
    // this must be called after initialisation of the ADC or it will be ignored
}

float getFrequency()
{
    float frequency =0;
    float averageVoltage =0;
    float lastVoltage = 0; 
    int crossings= 0;

    for (size_t i = 0; i < buffer.elementCounter; i++)
    {
        averageVoltage += (float)buffer.data[i];


    }
    
    averageVoltage = (averageVoltage/buffer.elementCounter);


    for (int i = 0; i < buffer.elementCounter; i++)
    {
        if ((lastVoltage<averageVoltage && buffer.data[i]>averageVoltage) || (lastVoltage>averageVoltage && buffer.data[i]<averageVoltage))
        {
            crossings++;
        }

        lastVoltage = buffer.data[i];
        
    }

    frequency = crossings/2;   
    free(buffer_data);
    return frequency;

}

void stopSampling()
{
    adc_power_off();
    timer_pause(TIMER_GROUP_0, TIMER_0);
}