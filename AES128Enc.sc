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

behavior AES128Enc {
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

	//key scheduler instance
	keySched128 key_inst( isEncode );  // for encode
	
	//instances of rounds
	firstRound128  first_inst1(round0, isEncode);
	normalRound128 normal_inst2(round1, isEncode);
	normalRound128 normal_inst3(round2, isEncode);
	normalRound128 normal_inst4(round3, isEncode);
	normalRound128 normal_inst5(round4, isEncode);
	normalRound128 normal_inst6(round5, isEncode);
	normalRound128 normal_inst7(round6, isEncode);
	normalRound128 normal_inst8(round7, isEncode);
	normalRound128 normal_inst9(round8, isEncode);
	normalRound128 normal_inst10(round9, isEncode);
	finalRound128  final_inst10(round10, isEncode);

	void main (void){
		fsm{
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
			final_inst10 : {break;}
		}
	}
};
