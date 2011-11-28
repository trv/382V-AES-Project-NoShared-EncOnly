#include "shared.h"
import "c_queue";

import "addRoundKey128";
import "invByteSub128";
import "invMixColumns128";
import "invShiftRows128";

behavior invNormalRound128 ( unsigned char round, unsigned char isEncode) {
//i_receiver stateIn, i_receiver keyIn, i_sender stateOut){
//	const unsigned long queueSize = 1024;
//	c_queue queueShiftByte(queueSize), queueAddMix(queueSize), queueByteAdd(queueSize);

	invByteSub128 invbyte_inst(round, isEncode); //queueShiftByte, queueByteAdd);

	invShiftRow128 invshift_inst(round, isEncode); //stateIn, queueShiftByte);

	invMixColumns128 invmix_inst(round, isEncode); //queueAddMix, stateOut);

	addRoundKey128 add_inst(round, isEncode); //queueByteAdd, keyIn, queueAddMix);

	void main (void){
		fsm {
			add_inst : {goto invmix_inst;}
			invmix_inst : {goto invshift_inst;}
			invshift_inst : {goto invbyte_inst;}
			invbyte_inst : {break;}
		}
	}
};
