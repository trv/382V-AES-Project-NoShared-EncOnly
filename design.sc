#define DEBUG_DESIGN 0

import "c_queue";
import "AES128Enc";
import "AES128Dec";
import "read";
import "write";

behavior Design(i_receiver qEnc, i_receiver qEncKey, i_sender qEncOut, 
                i_receiver qDec, i_receiver qDecKey, i_sender qDecOut) {

    const unsigned long size = 1024;

    c_queue read2EncText(size), read2DecText(size), Enc2WriteText(size), Dec2WriteText(size);
    c_queue read2EncKey(size), read2DecKey(size);

    Read readEnc(qEnc, qEncKey, read2EncText, read2EncKey);
    Read readDec(qDec, qDecKey, read2DecText, read2DecKey);
    Write writeEnc(Enc2WriteText, qEncOut);
    Write writeDec(Dec2WriteText, qDecOut);

    //AES Encryption Instance
    AES128Enc aes_enc_inst(read2EncText, read2EncKey, Enc2WriteText);

    //AES Decryption Instance
    AES128Dec aes_dec_inst(read2DecText, read2DecKey, Dec2WriteText);

    void main(void) {
        par {
            readEnc;
            aes_enc_inst;
            writeEnc;

            readDec;
            aes_dec_inst;
            writeDec;
        }
    }


};
