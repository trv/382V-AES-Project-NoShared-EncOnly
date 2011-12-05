#define DEBUG_ROUND 1

import "byteSub128";
import "shiftRow128";
import "addRoundKey128";

#if DEBUG_ROUND
behavior finalRound128(in unsigned char key[16], in unsigned char block_in[16], inout unsigned char block_out[16]) {
#else
behavior finalRound128(in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) {
#endif

  unsigned char block1[16];
  unsigned char block2[16];

	byteSub128 byte_inst(block_in, block1);
	shiftRow128 shift_inst(block1, block2);
	addRoundKey128 add_inst(key, block2, block_out);

	void main (void){
		fsm {
			byte_inst : {goto shift_inst;}
			shift_inst : {goto add_inst;}
			add_inst : {break;}
		}
	}
};
