#define DEBUG_MON_ENC 0

#if DEBUG_MON_ENC
#include <stdio.h>
#endif

import "c_queue";

behavior monitor_enc(i_receiver blockIn, i_sender blockOut){
	unsigned char block[16];
	void main (void){
		int i;
		unsigned long count;
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
