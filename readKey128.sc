#include "shared.h"
import "i_receiver";

behavior readKey128(i_receiver keyIn, in unsigned char isEncode) {

  void main(void) {
    if (isEncode) {
      keyIn.receive(&enc_key[0], sizeof(unsigned char) * 16);
    } else {
      keyIn.receive(&dec_key[0], sizeof(unsigned char) * 16);
    }
  }
};

