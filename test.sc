#define DEBUG_TEST 0

#include <stdio.h>

//import "c_queue";
import "stimulus";
//import "monitor_enc";
//import "monitor_dec";
import "design";




behavior Main (){

    unsigned char input_block[16];
    //unsigned char IV_block[16];
    unsigned char input_key[16];
    unsigned char output_block[16];
   
    unsigned char mode;
    unsigned short iter = 0;

	//const unsigned long qSize = 1024;
	
	//queues between instances
	//c_queue qDataStimDes(qSize), qKeyStimDes(qSize), qModeStimDes(qSize), qLengthStimDes(qSize), qIVStimDes(qSize), qBlockDesMon(qSize);
	//c_queue qBlockMonStim(qSize);

	//stimulus and monitor instances
	stimulus stim_inst(iter, mode, input_block, input_key, output_block);
//qDataStimDes, qKeyStimDes, qModeStimDes, qLengthStimDes, qIVStimDes, qBlockMonStim);
	//monitor_enc monitor_inst(qBlockDesMon, qBlockMonStim);
	
	Design design_inst(mode, input_block, input_key, output_block);

  void printOutput() {
    int i;
    for (i=0; i < 16; i++) {
      printf("%02hhX", output_block[i]);
    }
  }

	int main (void) {
    //printOutput();
		fsm{
			//stimulus
			stim_inst: {goto design_inst;}
			//monitor
			//monitor_inst;
			// runs both AES128Enc and AES128Dec in parallel
			design_inst: {if (iter != 2001) goto stim_inst;
                    break;}
    }
		return 0;
	}

};
