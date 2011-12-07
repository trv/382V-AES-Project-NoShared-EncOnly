#define DEBUG_ADD 0

#if DEBUG_ADD
#include <stdio.h>
#endif

behavior addRoundKey128(in unsigned char key[176], in unsigned char round, inout unsigned char block[16]) { 

	void main (void){
#if DEBUG_ADD
		int countBlock = 0;
		int countKey = 0;
#endif
		int i;
#if DEBUG_ADD
    printf("AddRoundKey received block %u\n", ++countBlock);
    printf("AddRoundKey received key %u\n", ++countKey);
#endif
    for (i = 0; i < 16; i ++){
      //bitwise XOR with key
      block[i] = block[i] ^ key[i + (16*round)];
    }
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
