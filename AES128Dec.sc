import "c_queue";

import "keySched128";
import "invByteSub128";
import "addRoundKey128";
import "invShiftRows128";
import "invMixColumns128";
import "invFirstRound128";
import "invNormalRound128";
import "invFinalRound128";

behavior AES128Dec (i_receiver block, i_receiver key, i_sender plainText){
	const unsigned long qSize = 1024;

	//queues between instances
	c_queue q12(qSize), q23(qSize), q34(qSize), q45(qSize), q56(qSize), q67(qSize), q78(qSize), q89(qSize), q910(qSize), q1011(qSize), qExpandedKey1(qSize), qExpandedKey2(qSize), qExpandedKey3(qSize), qExpandedKey4(qSize), qExpandedKey5(qSize), qExpandedKey6(qSize), qExpandedKey7(qSize), qExpandedKey8(qSize), qExpandedKey9(qSize), qExpandedKey10(qSize), qExpandedKey11(qSize);
	
	//key scheduler instance
	keySched128 key_inst(key, qExpandedKey1, qExpandedKey2, qExpandedKey3, qExpandedKey4, qExpandedKey5, qExpandedKey6, qExpandedKey7, qExpandedKey8, qExpandedKey9, qExpandedKey10, qExpandedKey11);
	
	//instances of rounds
	invFirstRound128  invfirst_inst1(block, qExpandedKey11, q12);
	invNormalRound128 invnormal_inst2(q12, qExpandedKey10, q23);
	invNormalRound128 invnormal_inst3(q23, qExpandedKey9, q34);
	invNormalRound128 invnormal_inst4(q34, qExpandedKey8, q45);
	invNormalRound128 invnormal_inst5(q45, qExpandedKey7, q56);
	invNormalRound128 invnormal_inst6(q56, qExpandedKey6, q67);
	invNormalRound128 invnormal_inst7(q67, qExpandedKey5, q78);
	invNormalRound128 invnormal_inst8(q78, qExpandedKey4, q89);
	invNormalRound128 invnormal_inst9(q89, qExpandedKey3, q910);
	invNormalRound128 invnormal_inst10(q910, qExpandedKey2, q1011);
	invFinalRound128  invfinal_inst10(q1011, qExpandedKey1, plainText);

	void main (void){
		par{
			//key generator
			key_inst;
			invfirst_inst1;
			invnormal_inst2;
			invnormal_inst3;
			invnormal_inst4;
			invnormal_inst5;
			invnormal_inst6;
			invnormal_inst7;
			invnormal_inst8;
			invnormal_inst9;
			invnormal_inst10;
			invfinal_inst10;
		}
	}
};
