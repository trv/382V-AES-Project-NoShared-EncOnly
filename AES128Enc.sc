#define DEBUG_AES128ENC 0
#if DEBUG_AES128ENC
#include <stdio.h>
#endif

import "keySched128";
import "firstRound128";
import "normalRound128";
import "finalRound128";

behavior AES128Enc(in unsigned char key[16], inout unsigned char block[16]) {

  unsigned char exp_key[176];
  unsigned char round = 0;

	//key scheduler instance
	keySched128 key_inst(key, exp_key);

	//instances of rounds
	firstRound128  first_inst(exp_key, block);
	normalRound128 normal_inst(exp_key, round, block);
	finalRound128  final_inst(exp_key, block);

	void main (void){
#if DEBUG_AES128ENC
    printf("starting AES128Enc\n");
#endif
    round = 1;
		fsm{
			key_inst : {goto first_inst;}
			first_inst : {goto normal_inst;}
			normal_inst : {
        if (++round == 10) goto final_inst;
        goto normal_inst;
      }
			final_inst : {break;}
		}
	}
};
