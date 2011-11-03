#define DEBUG_SHIFT 1

import "c_queue";

#if DEBUG_SHIFT
#include <stdio.h>
#endif

behavior shiftRow128(i_receiver blockIn, i_sender blockOut){

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
		unsigned char block[16];
		int i, j;
#if DEBUG_SHIFT
		int count = 0;
#endif
		blockIn.receive(&block[0], sizeof(unsigned char) * 16);
		printf("ShiftRow block data received:\n");
		for (i = 0; i < 16; i++){
			printf("%02hhx ", block[i]);
		}
		printf("\n");
#if DEBUG_SHIFT
		printf("ShiftRow received block %u\n", ++count);
#endif
		//rotateLeft row j of block by j bytes 
		for (i = 1; i < 4; i++){
			for (j = i; j > 0; j--){
				rotateLeft(&block[i]);
			}
		}
		blockOut.send(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_SHIFT
		printf("ShiftRow sent block %u\n", count);
		printf("ShiftRow block data sent:\n");
		for (i = 0; i < 16; i++){
			printf("%02hhx ", block[i]);
		}
		printf("\n");
#endif
	}
};
