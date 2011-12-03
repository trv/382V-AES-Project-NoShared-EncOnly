#define DEBUG_CONTROLLER 0

#include "topShared.h"
#include "shared.h"


#if DEBUG_CONTROLLER
#include <stdio.h>
#endif

behavior controllerIn(in unsigned char modeIn){
	
	void main (void){
		unsigned char mode;
		unsigned long i;
		
	    mode = modeIn; //mode is used every time
#if DEBUG_CONTROLLER
		printf("ControllerIn: read in mode value: %hhu.\n", mode);
#endif

	    switch (mode) {
		case MODE_ECB_ENC: 
			for (i = 0; i < 16; i++) {
			    enc_block[i] = input_block[i];
			    enc_key[i] = input_key[i];
			} 
			break;
		case MODE_ECB_DEC:
			for (i = 0; i < length; i++){
				dec_block[i] = input_block[i];
			    dec_key[i] = input_key[i];
			}
			break;
		default:
			break;
		}
	}
};



behavior controllerOut(in unsigned char modeIn){
	
	void main (void){
		unsigned char mode;
		unsigned long i;
		
	    mode = modeIn; //mode is used every time
#if DEBUG_CONTROLLER
		printf("ControllerOut: read in mode value: %hhu.\n", mode);
#endif
		
	    switch (mode) {
		case MODE_ECB_ENC: 
			for (i = 0; i < 16; i++) {
			    output_block[i] = enc_block[i];
			} 
			break;
		case MODE_ECB_DEC:
			for (i = 0; i < length; i++) {
			    output_block[i] = dec_block[i];
			}
			break;
		default:
			break;
		}
	}
};

