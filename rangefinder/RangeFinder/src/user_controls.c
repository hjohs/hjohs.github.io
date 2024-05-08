/**************************************************************************//**
 *
 * @file user_controls.c
 *
 * @author Harrison Johs
 *
 * @brief Code to get inputs from the user.
 *
 ******************************************************************************/

/*
 * RangeFinder GroupLab assignment and starter code (c) 2023 Christopher A. Bohn
 * RangeFinder solution (c) the above-named students
 */

#include <CowPi.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "user_controls.h"
#include "shared_variables.h"
#include "outputs.h"
#include "interrupt_support.h"

volatile int currentState;
volatile bool ultraPulse;
volatile bool buttonPressed = false;
void initialize_controls(void) {
    ultraPulse = false;

}

void manage_controls(void) {
    if (digitalRead(14) == 0 && digitalRead(15) == 0) {
    currentState = 1;
    } else if (digitalRead(14) == 1 && digitalRead(15) == 0) {
    currentState = 2;
    } else if (digitalRead(14) == 1 && digitalRead(15) == 1) {
    currentState = 3;
    } else if (digitalRead(14) == 0 && digitalRead(15) == 1) {
    currentState = 4;
    }
    
    if (currentState == 2 && digitalRead(2) == 0 && !buttonPressed) {
        ultraPulse = true;
        buttonPressed = true;
        display_string(2,sensorTimerStart);
    } else if (digitalRead(2) == 1) {
        buttonPressed = false;
    }
}
