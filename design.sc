#define DEBUG_DESIGN 0
#if DEBUG_DESIGN
#include <stdio.h>
#endif

#include "topShared.h"

import "AES128Enc";

behavior Design(in unsigned char mode, in unsigned char input_key[16], inout unsigned char block[16]) {

	//AES Encryption Instance
	AES128Enc aes_enc_inst(input_key, block);

	void main(void) {
    //printf("starting design...\n");
    aes_enc_inst;
	}
};
