#define DEBUG_ADD 0

#if DEBUG_ADD
#include <stdio.h>
#endif

#if DEBUG_ADD
behavior addRoundKey128(in unsigned char key[16], in unsigned char block_in[16], inout unsigned char block_out[16]) { 
#else
behavior addRoundKey128(in unsigned char key[16], in unsigned char block_in[16], out unsigned char block_out[16]) { 
#endif

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
      block_out[i] = block_in[i] ^ key[i];
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
      printf("%02hhx ", block_out[i]);
    }
    printf("\n");
#endif
	}
};
