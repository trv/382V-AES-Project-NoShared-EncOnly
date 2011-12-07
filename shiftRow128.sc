#define DEBUG_SHIFT 0

#if DEBUG_SHIFT
#include <stdio.h>
#endif

behavior shiftRow128(inout unsigned char block[16]) {

	void main (void){
    unsigned char b[16];
    unsigned char i;
#if DEBUG_SHIFT
		int count = 0;
#endif
#if DEBUG_SHIFT
		printf("ShiftRow received block %u\n", ++count);
		printf("ShiftRow block data received:\n");
		for (i = 0; i < 16; i++){
			printf("%02hhx ", block[i]);
		}
		printf("\n");
#endif

		b[0]  = block[0];
		b[1]  = block[5];
		b[2]  = block[10];
		b[3]  = block[15];
		b[4]  = block[4];
		b[5]  = block[9];
		b[6]  = block[14];
		b[7]  = block[3];
		b[8]  = block[8];
		b[9]  = block[13];
		b[10] = block[2];
		b[11] = block[7];
		b[12] = block[12];
		b[13] = block[1];
		b[14] = block[6];
		b[15] = block[11];

    for (i=0; i < 16; i++) block[i] = b[i];
		
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
