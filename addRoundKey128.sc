#define DEBUG_ADD 0

#include "shared.h"
import "c_queue";
#if DEBUG_ADD
#include <stdio.h>
#endif

// `round` is the current round so we know what part of the key to use, starting with round 0
// `isEncode` is 1 if we're using enc_*, or 0 if we're using dec_*
behavior addRoundKey128( in unsigned char round, in unsigned char isEncode ) { 
//i_receiver blockIn, i_receiver keyIn, i_sender blockOut){

	void main (void){
		//unsigned char block[16];
		//unsigned char key[16];
#if DEBUG_ADD
		int countBlock = 0;
		int countKey = 0;
#endif
		int i;
    //blockIn.receive(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_ADD
    //printf("AddRoundKey received block %u\n", ++countBlock);
#endif
    //keyIn.receive(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_ADD
    //printf("AddRoundKey received key %u\n", ++countKey);
#endif


    if (isEncode) {
      for (i = 0; i < 16; i ++){
        //bitwise XOR with key
        enc_block[i] = enc_block[i] ^ enc_key[i + (round << 4)];
      }
    } else {  // decode
      for (i = 0; i < 16; i ++){
        //bitwise XOR with key
        dec_block[i] = dec_block[i] ^ dec_key[i + (round << 4)];
      }
    }


//    blockOut.send(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_ADD
    printf("AddRoundKey send block %u\n", countBlock);
    printf("AddRoundKey key data:\n");
    for (i = 0; i < 16; i++){
      printf("%02hhx ", key[i]);
    }
    printf("\n");
    printf("AddRoundKey block data:\n");
    for (i = 0; i < 16; i++){
      printf("%02hhx ", block[i]);
    }
    printf("\n");
#endif
	}
};
