#include "shared.h"
import "c_queue";

import "invByteSub128";
import "invShiftRows128";
import "addRoundKey128";

behavior invFinalRound128( unsigned char round, unsigned char isEncode) {
//i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
//	const unsigned long queueSize = 1024;
//	c_queue queueShiftByte(queueSize), queueByteAdd(queueSize);

	invByteSub128 invbyte_inst(round, isEncode); //queueShiftByte, queueByteAdd);

	invShiftRow128 invshift_inst(round, isEncode); //stateIn, queueShiftByte);

	addRoundKey128 add_inst(round, isEncode); //queueByteAdd, keyIn, stateOut);

	void main (void){
//		par{
			add_inst;
			invshift_inst;
			invbyte_inst;
//		}
	}
};
