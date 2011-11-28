import "c_queue";

import "keySched128";
import "invByteSub128";
import "addRoundKey128";
import "invShiftRows128";
import "invMixColumns128";
import "invFirstRound128";
import "invNormalRound128";
import "invFinalRound128";
import "readKey128";
import "readBlock128";
import "writeBlock128";

unsigned char dec_block[16];
unsigned char dec_key[176];

behavior AES128Dec (i_receiver block, i_receiver qkey, i_sender plainText){
  unsigned char isEncode = (unsigned char) 0;
  unsigned char round0 = (unsigned char) 0;
  unsigned char round1 = (unsigned char) 1;
  unsigned char round2 = (unsigned char) 2;
  unsigned char round3 = (unsigned char) 3;
  unsigned char round4 = (unsigned char) 4;
  unsigned char round5 = (unsigned char) 5;
  unsigned char round6 = (unsigned char) 6;
  unsigned char round7 = (unsigned char) 7;
  unsigned char round8 = (unsigned char) 8;
  unsigned char round9 = (unsigned char) 9;
  unsigned char round10 = (unsigned char) 10;

//	const unsigned long qSize = 1024;
//  unsigned char key[176];

	//queues between instances
	//c_queue q12(qSize), q23(qSize), q34(qSize), q45(qSize), q56(qSize), q67(qSize), q78(qSize), q89(qSize), q910(qSize), q1011(qSize), qExpandedKey1(qSize), qExpandedKey2(qSize), qExpandedKey3(qSize), qExpandedKey4(qSize), qExpandedKey5(qSize), qExpandedKey6(qSize), qExpandedKey7(qSize), qExpandedKey8(qSize), qExpandedKey9(qSize), qExpandedKey10(qSize), qExpandedKey11(qSize);
	
  readKey128 readKey_inst(qkey, isEncode);
  readBlock128 readBlock_inst(block, isEncode);
  writeBlock128 writeBlock_inst(plainText, isEncode);

	//key scheduler instance
	keySched128 key_inst( isEncode );  
//key, qExpandedKey1, qExpandedKey2, qExpandedKey3, qExpandedKey4, qExpandedKey5, qExpandedKey6, qExpandedKey7, qExpandedKey8, qExpandedKey9, qExpandedKey10, qExpandedKey11);
	
	//instances of rounds
	invFinalRound128  invfirst_inst1(round10, isEncode);  //block, qExpandedKey11, q12);
	invNormalRound128 invnormal_inst2(round9, isEncode);  //q12, qExpandedKey10, q23);
	invNormalRound128 invnormal_inst3(round8, isEncode);  //q23, qExpandedKey9, q34);
	invNormalRound128 invnormal_inst4(round7, isEncode);  //q34, qExpandedKey8, q45);
	invNormalRound128 invnormal_inst5(round6, isEncode);  //q45, qExpandedKey7, q56);
	invNormalRound128 invnormal_inst6(round5, isEncode);  //q56, qExpandedKey6, q67);
	invNormalRound128 invnormal_inst7(round4, isEncode);  //q67, qExpandedKey5, q78);
	invNormalRound128 invnormal_inst8(round3, isEncode);  //q78, qExpandedKey4, q89);
	invNormalRound128 invnormal_inst9(round2, isEncode);  //q89, qExpandedKey3, q910);
	invNormalRound128 invnormal_inst10(round1, isEncode);  //q910, qExpandedKey2, q1011);
	invFirstRound128  invfinal_inst10(round0, isEncode);  //q1011, qExpandedKey1, plainText);

	void main (void){
		fsm{
      readBlock_inst : {goto readKey_inst;}
      readKey_inst : {goto key_inst;}
			key_inst : {goto invfirst_inst1;}
			invfirst_inst1 : {goto invnormal_inst2;}
			invnormal_inst2 : {goto invnormal_inst3;}
			invnormal_inst3 : {goto invnormal_inst4;}
			invnormal_inst4 : {goto invnormal_inst5;}
			invnormal_inst5 : {goto invnormal_inst6;}
			invnormal_inst6 : {goto invnormal_inst7;}
			invnormal_inst7 : {goto invnormal_inst8;}
			invnormal_inst8 : {goto invnormal_inst9;}
			invnormal_inst9 : {goto invnormal_inst10;}
			invnormal_inst10 : {goto invfinal_inst10;}
			invfinal_inst10 : {goto writeBlock_inst;}
      writeBlock_inst : {goto readBlock_inst;}
		}
	}
};
