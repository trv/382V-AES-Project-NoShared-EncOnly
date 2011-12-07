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

behavior stimulus(inout unsigned short iter, out unsigned char mode, inout unsigned char input_key[16], inout unsigned char block[16]) {

#include "keys.h"

  int loop = 0;
  unsigned char plaintext[16];

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
    char *text;

    if (iter == 0 && loop == 0) {
      printf("[ENCRYPT]\n");

      text = keys[loop];
      for (i=0; i < 16; i++) {
        sscanf(text, "%2hhx", &input_key[i]);
        text += 2;
      }

      block[ 0] = 0xb9;
      block[ 1] = 0x14;
      block[ 2] = 0x5a;
      block[ 3] = 0x76;
      block[ 4] = 0x8b;
      block[ 5] = 0x7d;
      block[ 6] = 0xc4;
      block[ 7] = 0x89;
      block[ 8] = 0xa0;
      block[ 9] = 0x96;
      block[10] = 0xb5;
      block[11] = 0x46;
      block[12] = 0xf4;
      block[13] = 0x3b;
      block[14] = 0x23;
      block[15] = 0x1f;

      for (i=0; i < 16; i++) {
        plaintext[i] = block[i];
      }
/*
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
*/

      mode = 1; // ECB encrypt
    } else if (iter == 1000) {
      printf("\nCOUNT = %d\n", loop);
      printf("KEY = ");
      printBlock(input_key, 16);
      printf("\nPLAINTEXT = ");
      printBlock(plaintext, 16);
      printf("\nCIPHERTEXT = ");
      printBlock(block, 16);
      printf("\n");

      iter = 0;
      loop++;
      text = keys[loop];
      for (i=0; i < 16; i++) {
        sscanf(text, "%2hhx", &input_key[i]);
        text += 2;
      }
      for (i=0; i < 16; i++) {
        plaintext[i] = block[i];
      }

      if (loop == 100) {
        // done with encrypt test, so exit
        exit(0);
      }
    } 
		iter++;
  }
};
