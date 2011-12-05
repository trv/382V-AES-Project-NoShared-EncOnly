#include "shared.h"
import "i_sender";

behavior writeBlock128(i_sender blockOut, in unsigned char isEncode) {

  void main(void) {
    if (isEncode) {
      blockOut.send(&enc_block[0], sizeof(unsigned char) * 16);
    } else {
      blockOut.send(&dec_block[0], sizeof(unsigned char) * 16);
    }
  }
};

