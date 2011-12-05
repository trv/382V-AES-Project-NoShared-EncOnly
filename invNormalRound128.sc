#define DEBUG_INVROUND 0

#include "shared.h"

import "addRoundKey128";
import "invByteSub128";
import "invMixColumns128";
import "invShiftRows128";

#if DEBUG_INVROUND	
behavior invNormalRound128 (in unsigned char key[16], in unsigned char block_in[16], inout unsigned char block_out[16]) {
#else
behavior invNormalRound128 (in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) {
#endif

  unsigned char block1[16];
  unsigned char block2[16];
  unsigned char block3[16];

	invByteSub128 invbyte_inst(block3, block_out);
	invShiftRow128 invshift_inst(block2, block3);
	invMixColumns128 invmix_inst(block1, block2);
	addRoundKey128 add_inst(key, block_in, block1);

	void main (void){
		fsm {
			add_inst : {goto invmix_inst;}
			invmix_inst : {goto invshift_inst;}
			invshift_inst : {goto invbyte_inst;}
			invbyte_inst : {break;}
		}
	}
};
