import "c_queue";

import "byteSub128";
import "shiftRow128";
import "addRoundKey128";

behavior finalRound128( in unsigned char round, in unsigned char isEncode ) {
//i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
	//const unsigned long queueSize = 1024;
	//c_queue queueByteShift(queueSize), queueShiftAdd(queueSize);
	byteSub128 byte_inst(round, isEncode); //stateIn, queueByteShift);
	shiftRow128 shift_inst(round, isEncode); //queueByteShift, queueShiftAdd);
	addRoundKey128 add_inst(round, isEncode); //queueShiftAdd, keyIn, stateOut);

	void main (void){
		fsm {
			byte_inst : {goto shift_inst;}
			shift_inst : {goto add_inst;}
			add_inst : {break;}
		}
	}
};
