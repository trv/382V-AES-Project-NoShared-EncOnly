
#include "shared.h"

import "c_queue";

import "addRoundKey128";

behavior invFirstRound128( unsigned char round, unsigned char isEncode) {
//i_receiver stateIn, i_receiver roundKeyIn, i_sender stateOut){
	
	addRoundKey128 add_inst(round, isEncode); //stateIn, roundKeyIn, stateOut);

	void main (void){
		fsm {
			add_inst : {break;}
		}
	}

};
