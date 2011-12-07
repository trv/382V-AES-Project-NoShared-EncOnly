#define DEBUG_ROUND 0

import "addRoundKey128";

#if DEBUG_ROUND
behavior firstRound128(in unsigned char key[176], in unsigned char block_in[16], inout unsigned char block_out[16]) {
#else
behavior firstRound128(in unsigned char key[176], in unsigned char block_in[16], out unsigned char block_out[16]) {
#endif
	
  unsigned char round = 0;
	addRoundKey128 add_inst(key, round, block_in, block_out);

	void main (void){
    fsm{
			add_inst : {break;}
		}
	}
};
