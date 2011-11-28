import "c_queue";

import "addRoundKey128";

behavior firstRound128( in unsigned char round, in unsigned char isEncode ) {
//i_receiver stateIn, i_receiver roundKeyIn, i_sender stateOut){
	
	addRoundKey128 add_inst(round, isEncode); //stateIn, roundKeyIn, stateOut);

	void main (void){
    fsm{
			add_inst : {break;}
		}
	}

};
