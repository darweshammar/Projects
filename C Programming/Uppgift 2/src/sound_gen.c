#include "soc/syscon_reg.h"
#include "sound_gen.h"
#include "soc/sens_reg.h"
#include "driver/dac.h"

int startSound(int freq)
{

    REG_SET_FIELD(SENS_SAR_DAC_CTRL1_REG, SENS_SW_TONE_EN, 1);
    REG_SET_FIELD(SENS_SAR_DAC_CTRL2_REG, SENS_DAC_CW_EN1, 1);

    REG_SET_BITS(SENS_SAR_DAC_CTRL1_REG, SENS_SW_FSTEP, freq);
    REG_SET_BITS(SENS_SAR_DAC_CTRL2_REG, SENS_DAC_INV1, 2);

    REG_CLR_BIT(SENS_SAR_DAC_CTRL1_REG,SENS_DAC_SCALE1); 
    REG_CLR_BIT(SENS_SAR_DAC_CTRL1_REG,SENS_DAC_DC1); 

    dac_output_enable(DAC_CHANNEL_1);

    return freq;
}

void stopSound()
{

    dac_output_disable(DAC_CHANNEL_1);
}