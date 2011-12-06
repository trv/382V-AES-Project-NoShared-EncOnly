#define DEBUG_STIM 0
#define DEBUG_STIM_IV 0
#define CBC_VECTORS	"vectors/CBCMCT128.rsp"
#define ECB_VECTORS	"vectors/ECBMCT128.rsp"
#define ENCRYPT_LIST "encrypt.txt"
#define DECRYPT_LIST "decrypt.txt"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "topShared.h"

behavior stimulus(inout unsigned short iter, out unsigned char mode, out unsigned char input_block[16], out unsigned char input_key[16], in unsigned char output_block[16]) {

	bool checkBlock(unsigned char * golden, unsigned char * check, int length){
		int i;
		for (i = 0; i < length; i++){
			if (golden[i] != check[i]) return false;
		}
		return true;
	}

	void printBlock(unsigned char * text, int length){
		int i;
		for (i = 0; i < length; i++){
			printf("%02hhx", text[i]);
		}
	}

	void printBlockLn(unsigned char * text, int length){
			printBlock(text, length);
			printf("\n");
	}

	void main (void){
    unsigned char i;

    if (iter == 0) {
      input_key[ 0] = 0x13;
      input_key[ 1] = 0x9a;
      input_key[ 2] = 0x35;
      input_key[ 3] = 0x42;
      input_key[ 4] = 0x2f;
      input_key[ 5] = 0x1d;
      input_key[ 6] = 0x61;
      input_key[ 7] = 0xde;
      input_key[ 8] = 0x3c;
      input_key[ 9] = 0x91;
      input_key[10] = 0x78;
      input_key[11] = 0x7f;
      input_key[12] = 0xe0;
      input_key[13] = 0x50;
      input_key[14] = 0x7a;
      input_key[15] = 0xfd;

      input_block[ 0] = 0xb9;
      input_block[ 1] = 0x14;
      input_block[ 2] = 0x5a;
      input_block[ 3] = 0x76;
      input_block[ 4] = 0x8b;
      input_block[ 5] = 0x7d;
      input_block[ 6] = 0xc4;
      input_block[ 7] = 0x89;
      input_block[ 8] = 0xa0;
      input_block[ 9] = 0x96;
      input_block[10] = 0xb5;
      input_block[11] = 0x46;
      input_block[12] = 0xf4;
      input_block[13] = 0x3b;
      input_block[14] = 0x23;
      input_block[15] = 0x1f;

      mode = 1; // ECB encrypt
    } else {
      for (i=0; i<16; i++) {
        input_block[i] = output_block[i];
      }
    }

    if (iter == 1000) {
      printf("Monte Carlo Encrypt test: ");
      printBlock(output_block, 16);
      printf("\n");

      // done with encrypt test, so exit
      exit(0);
    }

    if (iter == 2000) {
      printf("Monte Carlo Decrypt Test: ");
      printBlock(output_block, 16);
      printf("\n");
      exit(0);
    }

		iter++;
  }
};
