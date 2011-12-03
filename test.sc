#define DEBUG_TEST 0

import "c_queue";
import "stimulus";
import "monitor_enc";
import "monitor_dec";
import "design";

#if DEBUG_TEST
#include <stdio.h>
#endif

unsigned char input_block[16];
unsigned char IV_block[16];
unsigned char input_key[16];
unsigned char output_block[16];

behavior Main (){

  unsigned char mode;
  unsigned char iter = 0;

	//const unsigned long qSize = 1024;
	
	//queues between instances
	//c_queue qDataStimDes(qSize), qKeyStimDes(qSize), qModeStimDes(qSize), qLengthStimDes(qSize), qIVStimDes(qSize), qBlockDesMon(qSize);
	//c_queue qBlockMonStim(qSize);

	//stimulus and monitor instances
	stimulus stim_inst(iter, mode);
//qDataStimDes, qKeyStimDes, qModeStimDes, qLengthStimDes, qIVStimDes, qBlockMonStim);
	//monitor_enc monitor_inst(qBlockDesMon, qBlockMonStim);
	
	Design design_inst(mode);

	int main (void) {
		fsm{
			//stimulus
			stim_inst: {goto design_inst;}
			//monitor
			//monitor_inst;
			// runs both AES128Enc and AES128Dec in parallel
			design_inst: {goto stim_inst;}
    }
		return 0;
	}
};
