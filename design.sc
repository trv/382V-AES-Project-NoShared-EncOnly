#define DEBUG_DESIGN 0

import "c_queue";
import "controller";
import "AES128Enc";
import "AES128Dec";
import "readInput";
import "writeOutput";

#include "topShared.h"

behavior Design(in unsigned char mode) {

	//controls the different block modes
	controllerIn control_in_inst(mode);
  controllerOut control_out_inst(mode);

	//AES Encryption Instance
	AES128Enc aes_enc_inst(qBlockContEnc, qKeyContEnc, qBlockEncCont);

	//AES Decryption Instance
	AES128Dec aes_dec_inst(qBlockContDec, qKeyContDec, qBlockDecCont);

	void main(void) {
		fsm {
			control_in_inst: {if (mode == MODE_ECB_ENC) goto aes_enc_inst;
                        if (mode == MODE_ECB_DEC) goto aes_dec_inst;}
			aes_enc_inst: {goto control_out_inst;}
			aes_dec_inst: {goto control_out_inst;}
      control_out_inst: {break;}
		}
	}
};
