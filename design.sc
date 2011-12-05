#define DEBUG_DESIGN 0
#if DEBUG_DESIGN
#include <stdio.h>
#endif

#include "topShared.h"

import "controller";
import "AES128Enc";
import "AES128Dec";


behavior Design(in unsigned char mode, in unsigned char input_block[16], in unsigned char input_key[16], out unsigned char output_block[16]) {

    unsigned char output_block_enc[16];
    unsigned char output_block_dec[16];

	//controls the different block modes
    controllerIn control_in_inst(mode);
	controllerOut control_out_inst(mode, output_block_enc, output_block_dec, output_block);

	//AES Encryption Instance
	AES128Enc aes_enc_inst(input_key, input_block, output_block_enc);

	//AES Decryption Instance
	AES128Dec aes_dec_inst(input_key, input_block, output_block_dec);

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
