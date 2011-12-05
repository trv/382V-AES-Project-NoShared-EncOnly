#define DEBUG_AES128ENC 0
#if DEBUG_AES128ENC
#include <stdio.h>
#endif

import "keySched128";
import "firstRound128";
import "normalRound128";
import "finalRound128";

#if DEBUG_AES128ENC
behavior AES128Enc(in unsigned char key[16], in unsigned char block_in[16], inout unsigned char block_out[16]) {
#else
behavior AES128Enc(in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) {
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
	firstRound128  first_inst1(key, block_in, block1);
	normalRound128 normal_inst2(key1, block1, block2);
	normalRound128 normal_inst3(key2, block2, block3);
	normalRound128 normal_inst4(key3, block3, block4);
	normalRound128 normal_inst5(key4, block4, block5);
	normalRound128 normal_inst6(key5, block5, block6);
	normalRound128 normal_inst7(key6, block6, block7);
	normalRound128 normal_inst8(key7, block7, block8);
	normalRound128 normal_inst9(key8, block8, block9);
	normalRound128 normal_inst10(key9, block9, block10);
	finalRound128  final_inst10(key10, block10, block_out);

	void main (void){
#if DEBUG_AES128ENC
    printf("starting AES128Enc\n");
#endif
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
