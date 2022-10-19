#include <stdio.h>
#include <limits.h>
#include "circular_buffer.h"

void initCircularBuffer(struct circularBuffer *bufferPtr, int *data, int maxLength)
{
  bufferPtr->data = data;
  bufferPtr->head = 0;
  bufferPtr->tail = 0;
  bufferPtr->maxLength = maxLength;
  bufferPtr->isEmpty = 0;
  bufferPtr->isFull = 0;
  bufferPtr->elementCounter = 0;
}

/* 
 * This function should check if the buffer pointed to by bufferPtr
 * contains one or more elements with the value specified by the 
 * 'value' argument.
 *
 * The function should return:
 *  - 'value' if the an element with the argument value was found in the queue.
 *  - INT_MIN (defined in limits.h) if value was not found.
 */

int contains(struct circularBuffer *bufferPtr, int value)
{

  for (size_t i = bufferPtr->tail; i < bufferPtr->head; i--)
  {
    if (bufferPtr->data[i] == value && bufferPtr->isEmpty != 0)
    {

      printf("\nBuffer contains: %d", value);
      return value;
    }

    else
    {
      printf("Buffer doesn't contain: %d", value);
      return INT_MIN;
    }
  }
  return 0;
}

int addElement(struct circularBuffer *bufferPtr, int value)
{

  if (bufferPtr->head == bufferPtr->tail && bufferPtr->elementCounter == 0)
  {

    bufferPtr->isEmpty = 1;
  }
  else if (bufferPtr->tail == bufferPtr->head && bufferPtr->elementCounter >0 )
  {
    bufferPtr->isFull = 1;
    bufferPtr->isEmpty = 0;
  }

  if (bufferPtr->elementCounter>=0)
  {

    bufferPtr->data[bufferPtr->tail] = value;
    int valueInBuffer = bufferPtr->data[bufferPtr->tail];

    bufferPtr->tail = (bufferPtr->tail + 1) % bufferPtr->maxLength;
    bufferPtr->elementCounter++;

    return value;
  }
  else if (bufferPtr->isFull)
  {
    printf("\nbuffer is full");
    return INT_MIN;
  }
  return 0;
}




int removeValue(struct circularBuffer *bufferPtr, int value)
{

  if (bufferPtr->isEmpty)
  {
     int isRemoved = 0;
    for (size_t i = 0; i < bufferPtr->elementCounter; i++)
    {
      
      int removedValue =  (int) removeHead(bufferPtr);

     
      if (removedValue == value)
      {
          isRemoved = 1;

      }else
      {
        addElement(bufferPtr, removedValue);
      }
      

    }

    if (isRemoved)
    {
      return value;
    }else
    {
        
      return INT_MIN;

    }
    
  }
  return INT_MIN;
}

  int removeHead(struct circularBuffer* bufferPtr)
  {

      if (bufferPtr->elementCounter>0)
      {

        bufferPtr->head = (bufferPtr->head + 1) % bufferPtr->maxLength;
        bufferPtr->elementCounter--;
      
      }
      else
      {
        printf("buffer is empty");
        return INT_MIN;
      }

      return 0;
    }
  

    void printBuffer(struct circularBuffer * bufferPtr)
    {
      if (bufferPtr->elementCounter>0)
      {

        for (size_t i = 0; i < bufferPtr->elementCounter; i++)
        {
          int index = (bufferPtr->head + i) % bufferPtr->maxLength;
          printf("\nValues in buffer: \n value: %d", bufferPtr->data[index]);
        }
        
      }
      else
      {
        printf("Buffer is empty");
      }
    }
    