#define DEBUG_INVSHIFT 0

import "c_queue";

#if DEBUG_INVSHIFT
#include <stdio.h>
#endif

behavior invShiftRow128(i_receiver blockIn, i_sender blockOut){

	//rotateRights in place 32 bits (in 4 unsigned chars) one byte	
	void rotateRight (unsigned char * word32){
		unsigned char tempChar;
		tempChar = word32[12];
		word32[12] = word32[8];
		word32[8] = word32[4];
		word32[4] = word32[0];
		word32[0] = tempChar;
	}

	void main (void){
		unsigned char block[16];
		int i, j;
#if DEBUG_INVSHIFT
		int count = 0;
#endif
		//for (;;) {
			blockIn.receive(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_INVSHIFT
			printf("InvShiftRow received block %u\n", ++count);
			printf("InvShiftRow block data received:\n");
			for (i = 0; i < 16; i++){
				printf("%02hhx ", block[i]);
			}
			printf("\n");
#endif
			//rotateRight row j of block by j bytes 
			for (i = 1; i < 4; i++){
				for (j = i; j > 0; j--){
					rotateRight(&block[i]);
				}
			}
			blockOut.send(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_INVSHIFT
			printf("InvShiftRow sent block %u\n", count);
			printf("InvShiftRow block data sent:\n");
			for (i = 0; i < 16; i++){
				printf("%02hhx ", block[i]);
			}
			printf("\n");
#endif
		//}
	}
};
