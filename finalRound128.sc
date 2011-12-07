#define DEBUG_ROUND 0

import "byteSub128";
import "shiftRow128";
import "addRoundKey128";

behavior finalRound128(in unsigned char key[176], inout unsigned char block[16]) {

  unsigned char round = 10;

	byteSub128 byte_inst(block);
	shiftRow128 shift_inst(block);
	addRoundKey128 add_inst(key, round, block);

	void main (void){
		fsm {
			byte_inst : {goto shift_inst;}
			shift_inst : {goto add_inst;}
			add_inst : {break;}
		}
	}
};
