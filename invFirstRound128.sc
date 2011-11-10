import "c_queue";

import "addRoundKey128";

behavior invFirstRound128(i_receiver stateIn, i_receiver roundKeyIn, i_sender stateOut){
	
	addRoundKey128 add_inst(stateIn, roundKeyIn, stateOut);

	void main (void){
		par{
			add_inst;
		}
	}

};
