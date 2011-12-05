#define DEBUG_MON_ENC 1

#if DEBUG_MON_ENC
#include <stdio.h>
#endif

import "c_queue";

behavior monitor_enc(i_receiver blockIn, i_sender blockOut){
	void main (void){
		unsigned char block[16];
#if DEBUG_MON_ENC
		int i;
		unsigned long count;
#endif
		for (;;){
			blockIn.receive(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_MON_ENC
			printf("MonEnc: Received ciphertext #%lu = ", ++count);
			for (i = 0; i < 16; i++){
				printf("%02hhx", block[i]);
			}
			printf("\n");
#endif
			blockOut.send(&block[0], sizeof(unsigned char) * 16);
		}
	}
};
