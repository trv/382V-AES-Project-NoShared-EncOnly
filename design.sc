#define DEBUG_DESIGN 0
//#include <stdio.h>

import "controller";
import "AES128Enc";
import "AES128Dec";

#include "topShared.h"

behavior Design(in unsigned char mode) {

	//controls the different block modes
	controllerIn control_in_inst(mode);
  controllerOut control_out_inst(mode);

	//AES Encryption Instance
	AES128Enc aes_enc_inst;

	//AES Decryption Instance
	AES128Dec aes_dec_inst;

	void main(void) {
    //printf("starting design...\n");
		fsm {
			control_in_inst: {if (mode == MODE_ECB_ENC) goto aes_enc_inst;
                        if (mode == MODE_ECB_DEC) goto aes_dec_inst;}
			aes_enc_inst: {goto control_out_inst;}
			aes_dec_inst: {goto control_out_inst;}
      control_out_inst: {break;}
		}
	}
};
