import "c_queue";

import "invByteSub128";
import "invShiftRows128";
import "addRoundKey128";

behavior invFinalRound128(i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
	const unsigned long queueSize = 1024;
	c_queue queueShiftByte(queueSize), queueByteAdd(queueSize);

	invByteSub128 invbyte_inst(queueShiftByte, queueByteAdd);

	invShiftRow128 invshift_inst(stateIn, queueShiftByte);

	addRoundKey128 add_inst(queueByteAdd, keyIn, stateOut);

	void main (void){
		par{
			invbyte_inst;
			invshift_inst;
			add_inst;
		}
	}
};
