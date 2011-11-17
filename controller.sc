#define DEBUG_CONTROLLER 0

#define	MODE_ECB_DEC	1

import "i_receiver";
import "i_sender";

#if DEBUG_CONTROLLER
#include <stdio.h>
#endif

behavior controller(	i_receiver modeIn, i_receiver keyIn, i_receiver IVIn, i_receiver streamIn, i_receiver lengthIn,
			i_sender keyEncOut, i_sender IBEncOut, i_sender keyDecOut, i_sender IBDecOut,
			i_receiver OBEncIn, i_receiver OBDecIn,
			i_sender streamOut){
	
	void main (void){
		unsigned char mode;
		unsigned long length;
		unsigned char key[16], IV[16], PT[16], CT[16], IB[16];
		unsigned long i;
		for (;;){
#if DEBUG_CONTROLLER
			printf("Controller: waiting on mode ... ");
#endif
			//mode is used every time
			modeIn.receive(&mode, sizeof(unsigned char));
#if DEBUG_CONTROLLER
			printf(" received mode = %hhu.\n", mode);
#endif
			//length is used every time
#if DEBUG_CONTROLLER
			printf("Controller: waiting on length ... ");
#endif
			lengthIn.receive(&length, sizeof(unsigned long));
#if DEBUG_CONTROLLER
			printf(" received length = %lu.\n", length);
#endif
			//key is used every time
#if DEBUG_CONTROLLER
			printf("Controller: waiting on key ... ");
#endif
			keyIn.receive(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_CONTROLLER
			printf(" received key.\n");
#endif
			switch (mode) {
			case MODE_ECB_DEC: 
				for (i = 0; i < length; i++){
					//data to process
					streamIn.receive(&PT[0], sizeof(unsigned char) * 16);
					//key to use
					keyEncOut.send(&key[0], sizeof(unsigned char) * 16);
					//send to encryption
					IBEncOut.send(&PT[0], sizeof(unsigned char) * 16);
					//receive from encryption
					OBEncIn.receive(&CT[0], sizeof(unsigned char) * 16);
					//return encrypted data
					streamOut.send(&CT[0], sizeof(unsigned char) * 16);
				}
				break;
			default:
				break;
			}
		}
	}
};
