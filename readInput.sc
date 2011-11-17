#define DEBUG_READ 0

import "i_receiver";
import "i_sender";

#if DEBUG_READ
#include <stdio.h>
#endif

behavior readInput(i_receiver qData, i_receiver qKey, i_receiver qLength, i_receiver qMode, i_receiver qIV, i_sender qDataOut, i_sender qKeyOut, i_sender qIVOut, i_sender qLengthOut, i_sender qModeOut) {
	unsigned long length, i;
	unsigned char mode;
	unsigned char key[16];
	unsigned char block[16];
	unsigned char IV[16];
	
	void main (void) {
		while (true) {
#if DEBUG_READ 
			printf("Read: Waiting to receive mode ...");
#endif
			qMode.receive(&mode, sizeof(unsigned char));
#if DEBUG_READ
			printf(" received mode = %hhu.\n", mode);
#endif
#if DEBUG_READ 
			printf("Read: Waiting to receive length ...");
#endif
			qLength.receive(&length, sizeof(unsigned long));
#if DEBUG_READ
			printf(" received length = %lu.\n", length);
#endif
#if DEBUG_READ 
			printf("Read: Waiting to receive key ...");
#endif
			qKey.receive(&key[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
			printf(" received key.\n");
#endif
			if (mode != 1){
#if DEBUG_READ 
				printf("Read: Waiting to receive IV ...");
#endif
				qKey.receive(&IV[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
				printf(" received IV.\n");
#endif
			}
			//received all the metadata
			//send out all the metadata

#if DEBUG_READ
			printf("Read: Sent ... ");
#endif
			qKeyOut.send(&key[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
			printf(" key ");
#endif
			qLengthOut.send(&length, sizeof(unsigned long));
#if DEBUG_READ
			printf(" length ");
#endif
			qModeOut.send(&mode, sizeof(unsigned char));
#if DEBUG_READ
			printf(" mode ");
#endif
			if (mode != 1) {
				qIVOut.send(&IV[0], sizeof(unsigned char) * 16);
#if DEBUG_READ
				printf(" IV ");
#endif
			}
#if DEBUG_READ
			printf(".\n");
#endif

			//now receive all the data blocks
			for (i = 0; i < length; i ++){
#if DEBUG_READ
				printf("\nRead: waiting to receive block %lu ...", i);
#endif
				qData.receive(&block[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
				printf(" received block %lu.\n", i);
				printf("Read: sending block %lu ...", i);
#endif
				qDataOut.send(&block[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
				printf(" sent block %lu.\n", i);
#endif
			}
		}
	}
};
