#define DEBUG_ADD 0

import "c_queue";
#if DEBUG_ADD
#include <stdio.h>
#endif

behavior addRoundKey128(i_receiver blockIn, i_receiver keyIn, i_sender blockOut){

	void main (void){
		unsigned char block[16];
		unsigned char key[16];
#if DEBUG_ADD
		int countBlock = 0;
		int countKey = 0;
#endif
		int i;
		for (;;){
			blockIn.receive(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_ADD
			printf("AddRoundKey received block %u\n", ++countBlock);
#endif
			keyIn.receive(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_ADD
			printf("AddRoundKey received key %u\n", ++countKey);
#endif
			for (i = 0; i < 16; i ++){
				//bitwise XOR with key
				block[i] = block[i] ^ key[i];
			}
			blockOut.send(&block[0], sizeof(unsigned char) * 16);
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
	}
};
