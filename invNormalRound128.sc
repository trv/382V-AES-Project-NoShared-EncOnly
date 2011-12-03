#include "shared.h"
import "c_queue";

import "addRoundKey128";
import "invByteSub128";
import "invMixColumns128";
import "invShiftRows128";

behavior invNormalRound128 ( unsigned char round, unsigned char isEncode) {
	invByteSub128 invbyte_inst(round, isEncode);

	invShiftRow128 invshift_inst(round, isEncode);

	invMixColumns128 invmix_inst(round, isEncode);

	addRoundKey128 add_inst(round, isEncode);

	void main (void){
		fsm {
			add_inst : {goto invmix_inst;}
			invmix_inst : {goto invshift_inst;}
			invshift_inst : {goto invbyte_inst;}
			invbyte_inst : {break;}
		}
	}
};
