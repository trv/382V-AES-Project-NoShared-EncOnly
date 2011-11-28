
#include "shared.h"
import "c_queue";

behavior readBlock128(i_receiver blockIn, in unsigned char isEncode) {

  void main(void) {
    if (isEncode) {
      blockIn.receive(&enc_block[0], sizeof(unsigned char) * 16);
    } else {
      blockIn.receive(&dec_block[0], sizeof(unsigned char) * 16);
    }
  }
};

