import "c_queue";

import "addRoundKey128";

behavior firstRound128(in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) {
	
	addRoundKey128 add_inst(key, block_in, block_out);

	void main (void){
    fsm{
			add_inst : {break;}
		}
	}
};
