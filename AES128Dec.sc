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

  readKey128 readKey_inst(qkey, isEncode);
  readBlock128 readBlock_inst(block, isEncode);
  writeBlock128 writeBlock_inst(plainText, isEncode);

	//key scheduler instance
	keySched128 key_inst( isEncode );  
	
	//instances of rounds
	invFinalRound128  invfirst_inst1(round10, isEncode);
	invNormalRound128 invnormal_inst2(round9, isEncode);
	invNormalRound128 invnormal_inst3(round8, isEncode);
	invNormalRound128 invnormal_inst4(round7, isEncode);
	invNormalRound128 invnormal_inst5(round6, isEncode);
	invNormalRound128 invnormal_inst6(round5, isEncode);
	invNormalRound128 invnormal_inst7(round4, isEncode);
	invNormalRound128 invnormal_inst8(round3, isEncode);
	invNormalRound128 invnormal_inst9(round2, isEncode);
	invNormalRound128 invnormal_inst10(round1, isEncode);
	invFirstRound128  invfinal_inst10(round0, isEncode);

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
