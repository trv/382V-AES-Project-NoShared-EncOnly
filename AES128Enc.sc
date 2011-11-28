import "c_queue";

import "keySched128";
import "byteSub128";
import "addRoundKey128";
import "shiftRow128";
import "mixColumns128";
import "firstRound128";
import "normalRound128";
import "finalRound128";
import "readKey128";
import "readBlock128";
import "writeBlock128";

unsigned char enc_block[16];
unsigned char enc_key[176];

behavior AES128Enc (i_receiver block, i_receiver qkey, i_sender cipherText){
  unsigned char isEncode = (unsigned char) 1;
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

	//const unsigned long qSize = 1024;

	//queues between instances
	//c_queue q12(qSize), q23(qSize), q34(qSize), q45(qSize), q56(qSize), q67(qSize), q78(qSize), q89(qSize), q910(qSize), q1011(qSize), qExpandedKey1(qSize), qExpandedKey2(qSize), qExpandedKey3(qSize), qExpandedKey4(qSize), qExpandedKey5(qSize), qExpandedKey6(qSize), qExpandedKey7(qSize), qExpandedKey8(qSize), qExpandedKey9(qSize), qExpandedKey10(qSize), qExpandedKey11(qSize);
	
  readKey128 readKey_inst(qkey, isEncode);
  readBlock128 readBlock_inst(block, isEncode);
  writeBlock128 writeBlock_inst(cipherText, isEncode);

	//key scheduler instance
	keySched128 key_inst( isEncode );  // for encode
 //key, qExpandedKey1, qExpandedKey2, qExpandedKey3, qExpandedKey4, qExpandedKey5, qExpandedKey6, qExpandedKey7, qExpandedKey8, qExpandedKey9, qExpandedKey10, qExpandedKey11);
	
	//instances of rounds
	firstRound128  first_inst1(round0, isEncode); //block, qExpandedKey1, q12);
	normalRound128 normal_inst2(round1, isEncode);  //q12, qExpandedKey2, q23);
	normalRound128 normal_inst3(round2, isEncode);  //q23, qExpandedKey3, q34);
	normalRound128 normal_inst4(round3, isEncode);  //q34, qExpandedKey4, q45);
	normalRound128 normal_inst5(round4, isEncode);  //q45, qExpandedKey5, q56);
	normalRound128 normal_inst6(round5, isEncode);  //q56, qExpandedKey6, q67);
	normalRound128 normal_inst7(round6, isEncode);  //q67, qExpandedKey7, q78);
	normalRound128 normal_inst8(round7, isEncode);  //q78, qExpandedKey8, q89);
	normalRound128 normal_inst9(round8, isEncode);  //q89, qExpandedKey9, q910);
	normalRound128 normal_inst10(round9, isEncode);  //q910, qExpandedKey10, q1011);
	finalRound128  final_inst10(round10, isEncode);  //q1011, qExpandedKey11, cipherText);

	void main (void){
		fsm{
      readBlock_inst : {goto readKey_inst;}
      readKey_inst : {goto key_inst;}
			key_inst : {goto first_inst1;}
			first_inst1 : {goto normal_inst2;}
			normal_inst2 : {goto normal_inst3;}
			normal_inst3 : {goto normal_inst4;}
			normal_inst4 : {goto normal_inst5;}
			normal_inst5 : {goto normal_inst6;}
			normal_inst6 : {goto normal_inst7;}
			normal_inst7 : {goto normal_inst8;}
			normal_inst8 : {goto normal_inst9;}
			normal_inst9 : {goto normal_inst10;}
			normal_inst10 : {goto final_inst10;}
			final_inst10 : {goto writeBlock_inst;}
      writeBlock_inst : {goto readBlock_inst;}
		}
	}
};
