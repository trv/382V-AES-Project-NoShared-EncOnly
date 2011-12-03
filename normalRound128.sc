import "c_queue";

import "addRoundKey128";
import "byteSub128";
import "mixColumns128";
import "shiftRow128";

behavior normalRound128 ( in unsigned char round, in unsigned char isEncode ) {
	byteSub128 byte_inst(round, isEncode);

	shiftRow128 shift_inst(round, isEncode);

	mixColumns128 mix_inst(round, isEncode);

	addRoundKey128 add_inst(round, isEncode);

	void main (void){
		fsm {  // run these sequentially now
			byte_inst : {goto shift_inst;}
			shift_inst : {goto mix_inst;}
			mix_inst : {goto add_inst;}
			add_inst : {break;}
		}
	}
};
