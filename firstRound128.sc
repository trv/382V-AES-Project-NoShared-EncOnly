#define DEBUG_ROUND 0

import "addRoundKey128";

behavior firstRound128(in unsigned char key[176], inout unsigned char block[16]) {
	
  unsigned char round = 0;
	addRoundKey128 add_inst(key, round, block);

	void main (void){
    fsm{
			add_inst : {break;}
		}
	}
};
