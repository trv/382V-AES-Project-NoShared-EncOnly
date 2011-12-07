#define DEBUG_ROUND 0

import "addRoundKey128";
import "byteSub128";
import "mixColumns128";
import "shiftRow128";

behavior normalRound128 (in unsigned char key[176], in unsigned char round, inout unsigned char block[16] ) {

	byteSub128 byte_inst(block);
	shiftRow128 shift_inst(block);
	mixColumns128 mix_inst(block);
	addRoundKey128 add_inst(key, round, block);

	void main (void){
		fsm{ 
			byte_inst : {goto shift_inst;}
			shift_inst : {goto mix_inst;}
			mix_inst : {goto add_inst;}
			add_inst : {break;}
		}
	}
};
