import "c_queue";

import "addRoundKey128";

behavior firstRound128( in unsigned char round, in unsigned char isEncode ) {
	
	addRoundKey128 add_inst(round, isEncode);

	void main (void){
    fsm{
			add_inst : {break;}
		}
	}
};
