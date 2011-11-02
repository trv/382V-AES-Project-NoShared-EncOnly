import "c_queue";

behavior addRoundKey128(i_receiver blockIn, i_receiver keyIn, i_sender blockOut){
	unsigned char block[16];
	unsigned char key[16];

	void main (void){
		int i;
		blockIn.receive(&block[0], sizeof(unsigned char) * 16);
		keyIn.receive(&key[0], sizeof(unsigned char) * 16);
		for (i = 0; i < 16; i ++){
			//bitwise XOR with key
			block[i] = block[i] ^ key[i];
		}
		blockOut.send(&block[0], sizeof(unsigned char) * 16);
	}
};
