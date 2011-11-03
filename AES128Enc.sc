import "c_queue";

import "keySched128";
import "byteSub128";
import "addRoundKey128";
import "shiftRow128";
import "mixColumns128";
import "firstRound128";
import "normalRound128";
import "finalRound128";

behavior AES128Enc (i_receiver block, i_receiver key, i_sender cipherText){
	const unsigned long qSize = 1024;

	//queues between instances
	c_queue q12(qSize), q23(qSize), q34(qSize), q45(qSize), q56(qSize), q67(qSize), q78(qSize), q89(qSize), q910(qSize), q1011(qSize), qExpandedKey1(qSize), qExpandedKey2(qSize), qExpandedKey3(qSize), qExpandedKey4(qSize), qExpandedKey5(qSize), qExpandedKey6(qSize), qExpandedKey7(qSize), qExpandedKey8(qSize), qExpandedKey9(qSize), qExpandedKey10(qSize), qExpandedKey11(qSize);
	
	//key scheduler instance
	keySched128 key_inst(key, qExpandedKey1, qExpandedKey2, qExpandedKey3, qExpandedKey4, qExpandedKey5, qExpandedKey6, qExpandedKey7, qExpandedKey8, qExpandedKey9, qExpandedKey10, qExpandedKey11);
	
	//instances of rounds
	firstRound128  first_inst1(block, qExpandedKey1, q12);
	normalRound128 normal_inst2(q12, qExpandedKey2, q23);
	normalRound128 normal_inst3(q23, qExpandedKey3, q34);
	normalRound128 normal_inst4(q34, qExpandedKey4, q45);
	normalRound128 normal_inst5(q45, qExpandedKey5, q56);
	normalRound128 normal_inst6(q56, qExpandedKey6, q67);
	normalRound128 normal_inst7(q67, qExpandedKey7, q78);
	normalRound128 normal_inst8(q78, qExpandedKey8, q89);
	normalRound128 normal_inst9(q89, qExpandedKey9, q910);
	normalRound128 normal_inst10(q910, qExpandedKey10, q1011);
	finalRound128  final_inst10(q1011, qExpandedKey11, cipherText);

	void main (void){
		par{
			//key generator
			key_inst;
			first_inst1;
			normal_inst2;
			normal_inst3;
			normal_inst4;
			normal_inst5;
			normal_inst6;
			normal_inst7;
			normal_inst8;
			normal_inst9;
			normal_inst10;
			final_inst10;
		}
	}
};
