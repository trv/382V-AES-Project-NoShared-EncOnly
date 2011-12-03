import "c_queue";

import "byteSub128";
import "shiftRow128";
import "addRoundKey128";

behavior finalRound128( in unsigned char round, in unsigned char isEncode ) {
	byteSub128 byte_inst(round, isEncode);
	shiftRow128 shift_inst(round, isEncode);
	addRoundKey128 add_inst(round, isEncode);

	void main (void){
		fsm {
			byte_inst : {goto shift_inst;}
			shift_inst : {goto add_inst;}
			add_inst : {break;}
		}
	}
};
