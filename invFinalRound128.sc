#include "shared.h"

import "invByteSub128";
import "invShiftRows128";
import "addRoundKey128";

behavior invFinalRound128(in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) {

  unsigned char block1[16];
  unsigned char block2[16];

	invByteSub128 invbyte_inst(block2, block_out);
	invShiftRow128 invshift_inst(block1, block2);
	addRoundKey128 add_inst(key, block_in, block1);

	void main (void){
		fsm {
			add_inst : {goto invshift_inst;}
			invshift_inst : {goto invbyte_inst;}
			invbyte_inst : {break;}
		}
	}
};
