#define DEBUG_DESIGN 0

import "c_queue";
import "controller";
import "AES128Enc";
import "AES128Dec";
import "read";
import "write";

behavior Design(i_receiver qDataIn, i_receiver qKeyIn, i_receiver qModeIn, i_receiver qLengthIn, i_receiver qIVIn, i_sender dataOut) {

	const unsigned long size = 1024;
	c_queue qDataReadCont(size), qKeyReadCont(size), qLengthReadCont(size), qModeReadCont(size), qIVReadCont(size);
	c_queue qKeyContEnc(size), qBlockContEnc(size), qKeyContDec(size), qBlockContDec(size);
	c_queue qBlockEncCont(size), qBlockDecCont(size);
	c_queue qBlockContWrite(size);

	//software interface for input to this design
	Read read_inst(qDataIn, qKeyIn, qLengthIn, qModeIn, qIVIn, qDataReadCont, qKeyReadCont, qIVReadCont, qLengthReadCont, qModeReadCont);
	
	//controls the different block modes
	controller control_inst(qModeReadCont, qKeyReadCont, qIVReadCont, qDataReadCont, qLengthReadCont, qKeyContEnc, qBlockContEnc, qKeyContDec, qBlockContDec, qBlockEncCont, qBlockDecCont, qBlockContWrite);
	
	//software interface for output from this design
	Write write_inst(qBlockContWrite, dataOut);

	//AES Encryption Instance
	AES128Enc aes_enc_inst(qBlockContEnc, qKeyContEnc, qBlockEncCont);

	//AES Decryption Instance
	AES128Dec aes_dec_inst(qBlockContDec, qKeyContDec, qBlockDecCont);

	void main(void) {
		par {
			read_inst;
			aes_enc_inst;
			write_inst;
			control_inst;
			aes_dec_inst;
		}
	}


};
