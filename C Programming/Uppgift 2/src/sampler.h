#ifndef SAMPLER_H_
#define SAMPLER_H_

float getFrequency();

/**
* Starts sampling on a given pin with a given fequency.
* Parameter freq: the sampling frequency
*/
void startSampling(int freq);
/**
* Stops the sampling process.
*/
void stopSampling();

#endif