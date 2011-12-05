#define DEBUG_AES128DEC 1
#if DEBUG_AES128DEC
#include <stdio.h>
#endif

import "keySched128";
import "invByteSub128";
import "addRoundKey128";
import "invShiftRows128";
import "invMixColumns128";
import "invFirstRound128";
import "invNormalRound128";
import "invFinalRound128";

#if DEBUG_AES128DEC
behavior AES128Dec(in unsigned char key[16], in unsigned char block_in[16], inout unsigned char block_out[16]) {
#else
behavior AES128Dec(in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) {
#endif

  unsigned char block1[16];
  unsigned char block2[16];
  unsigned char block3[16];
  unsigned char block4[16];
  unsigned char block5[16];
  unsigned char block6[16];
  unsigned char block7[16];
  unsigned char block8[16];
  unsigned char block9[16];
  unsigned char block10[16];

  unsigned char key1[16];
  unsigned char key2[16];
  unsigned char key3[16];
  unsigned char key4[16];
  unsigned char key5[16];
  unsigned char key6[16];
  unsigned char key7[16];
  unsigned char key8[16];
  unsigned char key9[16];
  unsigned char key10[16];

	//key scheduler instance
	keySched128 key_inst(key, key1, key2, key3, key4, key5, key6, key7, key8, key9, key10);

	//instances of rounds
	invFinalRound128  invfinal_inst1(key10, block_in, block10);
	invNormalRound128 invnormal_inst2(key9, block10, block9);
	invNormalRound128 invnormal_inst3(key8, block9, block8);
	invNormalRound128 invnormal_inst4(key7, block8, block7);
	invNormalRound128 invnormal_inst5(key6, block7, block6);
	invNormalRound128 invnormal_inst6(key5, block6, block5);
	invNormalRound128 invnormal_inst7(key4, block5, block4);
	invNormalRound128 invnormal_inst8(key3, block4, block3);
	invNormalRound128 invnormal_inst9(key2, block3, block2);
	invNormalRound128 invnormal_inst10(key1, block2, block1);
	invFirstRound128  invfirst_inst10(key, block1, block_out);

	void main (void){
#if DEBUG_AES128DEC
			printf("Starting AES128DEC\n");
#endif
		fsm{
			key_inst : {goto invfinal_inst1;}
			invfinal_inst1 : {goto invnormal_inst2;}
			invnormal_inst2 : {goto invnormal_inst3;}
			invnormal_inst3 : {goto invnormal_inst4;}
			invnormal_inst4 : {goto invnormal_inst5;}
			invnormal_inst5 : {goto invnormal_inst6;}
			invnormal_inst6 : {goto invnormal_inst7;}
			invnormal_inst7 : {goto invnormal_inst8;}
			invnormal_inst8 : {goto invnormal_inst9;}
			invnormal_inst9 : {goto invnormal_inst10;}
			invnormal_inst10 : {goto invfirst_inst10;}
			invfirst_inst10 : {break;}
		}
	}
};
