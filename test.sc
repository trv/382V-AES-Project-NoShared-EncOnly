#define DEBUG_TEST 0

import "c_queue";
import "stimulus";
import "monitor_enc";
import "monitor_dec";
import "design";

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
	
    Design design_inst(qEnc, qEncKey, qEncOut, qDec, qDecKey, qDecOut);

	int main (void) {
		par{
			//stimulus
			stim_inst;
			//monitor
			monitor_enc_inst;
			monitor_dec_inst;
		    // runs both AES128Enc and AES128Dec in parallel
            design_inst;
        }
		return 0;
	}
};
