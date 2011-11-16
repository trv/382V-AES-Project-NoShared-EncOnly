#define	MODE_ECB_DEC	1

import "c_queue";

behavior controller(	i_receiver modeIn, i_receiver keyIn, i_receiver IVIn, i_receiver streamIn, i_receiver lengthIn,
			i_sender keyEncOut, i_sender IBEncOut, i_sender keyDecOut, IBDecOut,
			i_receiver OBEncIn, i_receiver OBDecIn,
			i_sender streamOut){
	
	void main (void){
		unsigned char mode;
		unsigned long length;
		unsigned char key[16], IV[16], PT[16], CT[16], IB[16];
		unsigned long i;
		for (;;){
			//mode is used every time
			modeIn.receive(&mode, sizeof(unsigned char));
			//length is used every time
			lengthIn.receive(&length, sizeof(unsigned long));
			//key is used every time
			keyIn.receive(&key[0], sizeof(unsigned char) * 16);
			switch (mode) {
			case MODE_ECB_DEC: 
				for (i = 0; i < length; i++){
					//data to process
					streamIn.receive(&PT[0], sizeof(unsigned char) * 16);
					//key to use
					keyEncOut.send(&key[0], sizeof(unsigned char) * 16);
					//send to encryption
					IBEncOut.send(&PT[0], sizeof(unsigned char) * 16);
					//receive from encryption
					OBEncIn.receive(&CT[0], sizeof(unsigned char) * 16);
					//return encrypted data
					streamOut.send(&CT[0], sizeof(unsigned char) * 16);
				}
				break;
			default:
				break;
			}
		}
	}
};
