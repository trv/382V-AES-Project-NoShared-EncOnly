
#include "shared.h"

import "c_queue";

import "addRoundKey128";

behavior invFirstRound128( unsigned char round, unsigned char isEncode) {
	
	addRoundKey128 add_inst(round, isEncode);

	void main (void){
		fsm {
			add_inst : {break;}
		}
	}
};
