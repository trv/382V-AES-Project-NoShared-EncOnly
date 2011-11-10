#define DEBUG_TEST 0

import "c_queue";
import "stimulus";
import "monitor";
import "AES128Enc";
import "AES128Dec";

#if DEBUG_TEST
#include <stdio.h>
#endif

behavior Main (){
	const unsigned long qSize = 1024;
	
	//queues between instances
	c_queue qIn(qSize), qOut(qSize), qKey(qSize);
	c_queue qEncDec(qSize);
	
	//stimulus and monitor instances
	stimulus stim_inst(qIn, qKey);
	monitor monitor_inst(qOut);
	
	//AES Encryption Instance
	AES128Enc aes_enc_inst(qIn, qKey, qEncDec);

	//AES Decryption Instance
	AES128Dec aes_dec_inst(qEncDec, qKey, qOut);

	int main (void) {
		par{
			//stimulus
			stim_inst;
			//monitor
			monitor_inst;
			//encryption
			aes_enc_inst;
			//decryption
			aes_dec_inst;
		}
		return 0;
	}
};
