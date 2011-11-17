import "i_receiver";
import "i_sender";

behavior writeOutput(i_receiver q, i_sender qOut) {
   
   unsigned char block[16];
    
    void main (void) {
        while (true) {
            q.receive(&block[0], (sizeof(unsigned char) * 16));
            qOut.send(&block[0], (sizeof(unsigned char) * 16));
        }
    }
};
