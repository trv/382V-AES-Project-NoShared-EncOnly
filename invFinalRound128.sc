#include "shared.h"
import "c_queue";

import "invByteSub128";
import "invShiftRows128";
import "addRoundKey128";

behavior invFinalRound128( unsigned char round, unsigned char isEncode) {
	invByteSub128 invbyte_inst(round, isEncode);

	invShiftRow128 invshift_inst(round, isEncode);

	addRoundKey128 add_inst(round, isEncode);

	void main (void){
		fsm {
			add_inst : {goto invshift_inst;}
			invshift_inst : {goto invbyte_inst;}
			invbyte_inst : {break;}
		}
	}
};
