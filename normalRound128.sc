import "c_queue";

import "addRoundKey128";
import "byteSub128";
import "mixColumns128";
import "shiftRow128";

behavior normalRound128 (i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
	const unsigned long queueSize = 1024;
	c_queue queueByteShift(queueSize), queueShiftMix(queueSize), queueMixAdd(queueSize);

	byteSub128 byte_inst(stateIn, queueByteShift);

	shiftRow128 shift_inst(queueByteShift, queueShiftMix);

	mixColumns128 mix_inst(queueShiftMix, queueMixAdd);

	addRoundKey128 add_inst(queueMixAdd, keyIn, stateOut);

	void main (void){
		par{
			byte_inst;
			shift_inst;
			mix_inst;
			add_inst;
		}
	}
};
