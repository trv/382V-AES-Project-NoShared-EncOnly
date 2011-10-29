#include <stdlib.h>
#include <stdio.h>

import "c_queue";


behavior monitor(i_receiver blockIn){
	unsigned char block[16];
	void main (void){
		int i;
		blockIn.receive(&block[0], sizeof(unsigned char) * 16);
		
		printf("Ciphertext = ");
		for (i = 0; i < 16; i++){
			printf("%02hhx", block[i]);
		}
		printf("\n");
		exit(0);
	}

};
