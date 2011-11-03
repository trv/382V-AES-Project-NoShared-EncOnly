import "c_queue";

import "byteSub128";
import "shiftRow128";
import "addRoundKey128";

behavior finalRound128(i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
	const unsigned long queueSize = 1024;
	c_queue queueByteShift(queueSize), queueShiftAdd(queueSize);
	byteSub128 byte_inst(stateIn, queueByteShift);
	shiftRow128 shift_inst(queueByteShift, queueShiftAdd);
	addRoundKey128 add_inst(queueShiftAdd, keyIn, stateOut);

	void main (void){
		par{
			byte_inst;
			shift_inst;
			add_inst;
		}
	}
};
