#define DEBUG_INVROUND 0

import "addRoundKey128";

#if DEBUG_INVROUND
behavior invFirstRound128(in unsigned char key[16], in unsigned char block_in[16], inout unsigned char block_out[16]) {
#else 
behavior invFirstRound128(in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) {
#endif
	
	addRoundKey128 add_inst(key, block_in, block_out);

	void main (void){
		fsm {
			add_inst : {break;}
		}
	}
};
