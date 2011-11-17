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
	c_queue qDataStimDes(qSize), qKeyStimDes(qSize), qModeStimDes(qSize), qLengthStimDes(qSize), qIVStimDes(qSize), qBlockDesMon(qSize);
	c_queue qBlockMonStim(qSize);

	//stimulus and monitor instances
	stimulus stim_inst(qDataStimDes, qKeyStimDes, qModeStimDes, qLengthStimDes, qIVStimDes, qBlockMonStim);
	monitor_enc monitor_inst(qBlockDesMon, qBlockMonStim);
	
	Design design_inst(qDataStimDes, qKeyStimDes, qModeStimDes, qLengthStimDes, qIVStimDes, qBlockDesMon);

	int main (void) {
		par{
			//stimulus
			stim_inst;
			//monitor
			monitor_inst;
			// runs both AES128Enc and AES128Dec in parallel
			design_inst;
	        }
		return 0;
	}
};
