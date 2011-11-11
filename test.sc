#define DEBUG_TEST 0

import "c_queue";
import "stimulus";
import "monitor_enc";
import "monitor_dec";
import "AES128Enc";
import "AES128Dec";

#if DEBUG_TEST
#include <stdio.h>
#endif

behavior Main (){
	const unsigned long qSize = 1024;
	
	//queues between instances
	c_queue qEnc(qSize), qDec(qSize), qEncOut(qSize), qDecOut(qSize), qEncKey(qSize), qDecKey(qSize);
	c_queue qEncMonStim(qSize), qDecMonStim(qSize);

	//stimulus and monitor instances
	stimulus stim_inst(qEnc, qDec, qEncKey, qDecKey, qEncMonStim, qDecMonStim);
	monitor_enc monitor_enc_inst(qEncOut, qEncMonStim);
	monitor_dec monitor_dec_inst(qDecOut, qDecMonStim);
	
	//AES Encryption Instance
	AES128Enc aes_enc_inst(qEnc, qEncKey, qEncOut);

	//AES Decryption Instance
	AES128Dec aes_dec_inst(qDec, qDecKey, qDecOut);

	int main (void) {
		par{
			//stimulus
			stim_inst;
			//monitor
			monitor_enc_inst;
			monitor_dec_inst;
			//encryption
			aes_enc_inst;
			//decryption
			aes_dec_inst;
		}
		return 0;
	}
};
