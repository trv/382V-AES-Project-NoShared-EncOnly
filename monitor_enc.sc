#include <stdio.h>

import "c_queue";


behavior monitor_enc(i_receiver blockIn, i_sender blockOut){
	unsigned char block[16];
	void main (void){
		int i;
		for (;;){
			blockIn.receive(&block[0], sizeof(unsigned char) * 16);
			printf("Monitor Encrypt: Received ciphertext = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", block[i]);
			}
			printf("\n");
			blockOut.send(&block[0], sizeof(unsigned char) * 16);
		}
	}
};
