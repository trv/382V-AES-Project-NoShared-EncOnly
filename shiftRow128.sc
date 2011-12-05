#define DEBUG_SHIFT 1

#include "shared.h"

#if DEBUG_SHIFT
#include <stdio.h>
#endif

#if DEBUG_SHIFT
behavior shiftRow128(in unsigned char block_in[16], inout unsigned char block_out[16]) {
#else
behavior shiftRow128(in unsigned char block_in[16], out unsigned char block_out[16]) {
#endif

	//rotateLefts in place 32 bits (in 4 unsigned chars) one byte	
	void rotateLeft (unsigned char * word32){
		unsigned char tempChar;
		tempChar = word32[0];
		word32[0] = word32[4];
		word32[4] = word32[8];
		word32[8] = word32[12];
		word32[12] = tempChar;
	}

	void main (void){
		int i, j;
#if DEBUG_SHIFT
		int count = 0;
#endif
#if DEBUG_SHIFT
			printf("ShiftRow received block %u\n", ++count);
			printf("ShiftRow block data received:\n");
			for (i = 0; i < 16; i++){
				printf("%02hhx ", block_in[i]);
			}
			printf("\n");
#endif
/*
			//rotateLeft row j of block by j bytes 
      for (i = 1; i < 4; i++){
        for (j = i; j > 0; j--){
          rotateLeft(&enc_block[i]);
        }
      }
*/
      block_out[0] = block_in[0];
      block_out[1] = block_in[1];
      block_out[2] = block_in[2];
      block_out[3] = block_in[3];

      block_out[4] = block_in[5];
      block_out[5] = block_in[6];
      block_out[6] = block_in[7];
      block_out[7] = block_in[4];

      block_out[8] = block_in[10];
      block_out[9] = block_in[11];
      block_out[10] = block_in[8];
      block_out[11] = block_in[9];

      block_out[12] = block_in[15];
      block_out[13] = block_in[12];
      block_out[14] = block_in[13];
      block_out[15] = block_in[14];

#if DEBUG_SHIFT
			printf("ShiftRow sent block %u\n", count);
			printf("ShiftRow block data sent:\n");
			for (i = 0; i < 16; i++){
				printf("%02hhx ", block_out[i]);
			}
			printf("\n");
#endif
	}
};
