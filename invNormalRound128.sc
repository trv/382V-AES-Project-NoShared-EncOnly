import "c_queue";

import "addRoundKey128";
import "invByteSub128";
import "invMixColumns128";
import "invShiftRows128";

behavior invNormalRound128 (i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
	const unsigned long queueSize = 1024;
	c_queue queueShiftByte(queueSize), queueAddMix(queueSize), queueByteAdd(queueSize);

	invByteSub128 invbyte_inst(queueShiftByte, queueByteAdd);

	invShiftRow128 invshift_inst(stateIn, queueShiftByte);

	invMixColumns128 invmix_inst(queueAddMix, stateOut);

	addRoundKey128 add_inst(queueByteAdd, keyIn, queueAddMix);

	void main (void){
		par{
			invbyte_inst;
			invshift_inst;
			invmix_inst;
			add_inst;
		}
	}
};
