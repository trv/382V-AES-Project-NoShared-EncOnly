#define TEST_LENGTH 1

#include <stdlib.h>
#include <stdio.h>

import "c_queue";


behavior monitor(i_receiver blockIn){
	unsigned char block[16];
	void main (void){
		int i;
		int j;
		for (j = 0; i < TEST_LENGTH; j++){
			blockIn.receive(&block[0], sizeof(unsigned char) * 16);
			printf("Monitor: Received text = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", block[i]);
			}
			printf("\n");
		}
		exit(0);
	}

};
