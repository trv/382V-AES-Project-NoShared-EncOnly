import "c_queue";

import "addRoundKey128";
import "byteSub128";
import "mixColumns128";
import "shiftRow128";

behavior normalRound128 ( in unsigned char round, in unsigned char isEncode ) {
// i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
	const unsigned long queueSize = 1024;
	//c_queue queueByteShift(queueSize), queueShiftMix(queueSize), queueMixAdd(queueSize);

	byteSub128 byte_inst(round, isEncode); //stateIn, queueByteShift);

	shiftRow128 shift_inst(round, isEncode); //queueByteShift, queueShiftMix);

	mixColumns128 mix_inst(round, isEncode); //queueShiftMix, queueMixAdd);

	addRoundKey128 add_inst(round, isEncode); //queueMixAdd, keyIn, stateOut);

	void main (void){
		fsm {  // run these sequentially now
			byte_inst : {goto shift_inst;}
			shift_inst : {goto mix_inst;}
			mix_inst : {goto add_inst;}
			add_inst : {break;}
		}
	}
};
