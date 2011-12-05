#define DEBUG_MON_DEC 1

#if DEBUG_MON_DEC
#include <stdio.h>
#endif

import "c_queue";

behavior monitor_dec(i_receiver blockIn, i_sender blockOut){
	unsigned char block[16];
	void main (void){
#if DEBUG_MON_DEC
		int i;
		unsigned long count;
#endif
		for (;;){
			blockIn.receive(&block[0], sizeof(unsigned char) * 16);
#if DEBUG_MON_DEC
			printf("MonDec: Received plaintexti #%lu = ", ++count);
			for (i = 0; i < 16; i++){
				printf("%02hhx", block[i]);
			}
			printf("\n");
#endif
			blockOut.send(&block[0], sizeof(unsigned char) * 16);
		}
	}
};
