#define DEBUG_READ 0

import "i_receiver";
import "i_sender";

#if DEBUG_READ
#include <stdio.h>
#endif

behavior Read(i_receiver qData, i_receiver qKey, i_receiver qLength, i_rceiver qMode i_receiver qIV, i_sender qDataOut, i_sender qKeyOut, i_sender qIVOut, i_sender qLengthOut, i_sender qModeOut) {
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
            printf(" received mode.\n");
#endif
#if DEBUG_READ 
            printf("Read: Waiting to receive length ...");
#endif
	    qLength.receive(&length, sizeof(unsigned long));
#if DEBUG_READ
            printf(" received length.\n");
#endif
#if DEBUG_READ 
            printf("Read: Waiting to receive key ...");
#endif
	    qKey.receive(&key[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
            printf(" received key.\n");
#endif
#if DEBUG_READ 
            printf("Read: Waiting to receive IV ...");
#endif
	    qKey.receive(&IV[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
            printf(" received IV.\n");
#endif
	    //received all the metadata
	    //send out all the metadata
            qKeyOut.send(&key[0], (sizeof(unsigned char) * 16));
	    qLengthOut.send(&length, sizeof(unsigned long));
	    qModeOut.send(&mode, sizeof(unsigned char));
	    qIVOut.send(&IV[0], sizeof(unsigned char) * 16);
	    //now receive all the data blocks
	    for (i = 0; i < length; i ++){
#if DEBUG_READ
	            printf("\nRead: waiting to receive block %lu ...", i);
#endif
        	    qData.receive(&block[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
	            printf(" received block.\n");
	            printf("Read: sending block ...");
#endif
	            qDataOut.send(&block[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
        	    printf(" sent block.\n");
#endif
	    }
        }
    }
};
