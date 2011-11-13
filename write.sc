import "i_receiver";
import "i_sender";

behavior Write(i_receiver q, i_sender qOut) {
   
   unsigned char ciphertext[16];
    
    void main (void) {
        while (true) {
            q.receive(&ciphertext[0], (sizeof(unsigned char) * 16));
            qOut.send(&ciphertext[0], (sizeof(unsigned char) * 16));
        }
    }


};
