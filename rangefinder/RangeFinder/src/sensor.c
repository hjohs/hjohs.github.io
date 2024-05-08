/**************************************************************************//**
 *
 * @file sensor.c
 *
 * @author Harrison Johs
 *
 * @brief Code to manage the distance sensor.
 *
 ******************************************************************************/

/*
 * RangeFinder GroupLab assignment and starter code (c) 2023 Christopher A. Bohn
 * RangeFinder solution (c) the above-named students
 */

#include <CowPi.h>
#include <limits.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include "sensor.h"
#include "shared_variables.h"
#include "outputs.h"
#include "interrupt_support.h"

volatile bool objectDetected;
volatile u_int32_t objectDistance;
volatile int rateOfApproach;
volatile int sensorState;
int ADC = 889;
cowpi_timer_t * timer = (cowpi_timer_t *)0x40054000;
volatile u_int32_t sensorTimerStart;
volatile u_int32_t sensorTimerEnd;
volatile u_int32_t pulseTime;
int cock = 1;

void handle_sensor_timer(void) {
    if (sensorState == 1) {
        sensorState = 2; 
    } else if (sensorState == 2) {
        sensorState = 3;
    } else if (sensorState == 4) {
        objectDetected = false;
        sensorState = 6;
    } else if (sensorState == 5) {
        objectDetected = true;
        sensorState = 6;
    } else if (sensorState == 6) {
        sensorState = 3;
    }
}

void handle_pulse_change(void) {
    if (digitalRead(16)) {
        reset_timer(1);
        sensorTimerStart = timer->raw_lower_word;
        sensorState = 4;
    } else if (digitalRead(16) && sensorState == 4) {
        sensorTimerEnd = timer->raw_lower_word;
        pulseTime = sensorTimerStart - sensorTimerEnd;
        sensorState = 5;
    }
}

void initialize_sensor(void) {
    objectDetected = false;
    register_timer_ISR(1,32768,handle_sensor_timer);
    sensorState = 1;
    register_pin_ISR((1L << 16),handle_pulse_change);
}

void manage_sensor(void) {
    if (ultraPulse) {
        digitalWrite(TRIGGER,HIGH);
        ultraPulse = false;
        u_int32_t start = timer->raw_lower_word;
        while (start - timer->raw_lower_word < 10) {}
        digitalWrite(TRIGGER,LOW);
    }
    
    char d[1];
    sprintf(d,"%u",currentState);
    display_string(3,d);
    if (objectDetected) {
        objectDistance = pulseTime * (256108888 - (121907 * 889));
        objectDistance >>= 33;
        char str[32];
        sprintf(str,"%u",objectDistance);
        display_string(1,str);
    } else {
        display_string(1,"NO OBJ DETECTED");
    }
}   
