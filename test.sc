#define DEBUG_TEST 0

#include <stdio.h>

import "stimulus";
import "design";

behavior AES (){

  //unsigned char IV_block[16];
  unsigned char input_key[16];
  unsigned char block[16];
 
  unsigned char mode;
  unsigned short iter = 0;

	//stimulus instance
	stimulus stim_inst(iter, mode, input_key, block);
	
	Design design_inst(mode, input_key, block);

  void printOutput() {
    int i;
    for (i=0; i < 16; i++) {
      printf("%02hhX", block[i]);
    }
  }

	void main (void) {
    //printOutput();
		fsm{
			//stimulus
			stim_inst: {goto design_inst;}
			// runs both AES128Enc and AES128Dec in parallel
			design_inst: {goto stim_inst;}
      }
	}

};


behavior Main (){
    AES aes;

    int main(void) {
        aes.main();
        return 0;
    }


};
