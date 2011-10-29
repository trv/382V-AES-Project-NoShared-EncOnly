
import "c_queue";

behavior shiftRow128(i_receiver blockIn, i_sender blockOut){
	unsigned char block[4][4];
	unsigned char blockShifted[4][4];
	int i, j;
	void main (void){
		blockIn.receive(&block, sizeof(unsigned char) * 16);
		//shift row x of block by x bytes 
		for (i = 0; i < 4; i++){
			for (j = 0; j < 4; j++){
				blockShifted[i][j] = block[i][(j+i) % 4];
			}
		}
		blockOut.send(&blockShifted, sizeof(unsigned char) * 16);
	}
};
