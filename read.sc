#define DEBUG_READ 0

import "i_receiver";
import "i_sender";

#if DEBUG_READ
#include <stdio.h>
#endif

behavior Read(i_receiver q, i_receiver qKey, i_sender qOut, i_sender qKeyOut) {
   
   unsigned char key[16];
   unsigned char plaintext[16];
    
    void main (void) {
        while (true) {
#if DEBUG_READ
            printf("\nRead: waiting to receive plaintext ...");
#endif
            q.receive(&plaintext[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
            printf(" received plaintext.\n");
            printf("Read: sending plaintext ...");
#endif

            qOut.send(&plaintext[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
            printf(" sent plaintext.\n");
            printf("Read: Waiting to receive key ...");
#endif
            qKey.receive(&key[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
            printf(" received key.\n");
            printf("Read: sending key ...");
#endif
            qKeyOut.send(&key[0], (sizeof(unsigned char) * 16));
#if DEBUG_READ
            printf(" sent key.\n");
#endif
        }
    }


};
