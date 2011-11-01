#define TEST_LENGTH 1
#define BYTE_SUB_VECTORS	"vectors/CBCGFSbox128.rsp"
#include <stdio.h>
#include <stdlib.h>

import "c_queue";
import "byteSub128";

behavior stimulus(i_sender blockOut) {
	void main (void){
		FILE *fp;
		char buffer[128];
		char * bufferPt;
		int count;
		int i;
		int j;
		unsigned char key[16], iv[16], plaintext[16], ciphertext[16];

		fp = fopen(BYTE_SUB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", BYTE_SUB_VECTORS);
			exit(1);
		}
		//advance to the first "COUNT" in the file
		for (fgets(buffer, 128, fp); buffer[0] != 'C'; fgets(buffer, 128, fp)) {}
		for (j = 0; j < TEST_LENGTH; j++){
			//parse the count number
			sscanf(buffer, "COUNT = %u", &count);
			//next line
			fgets(buffer, 128, fp);
			//parse key
			//find = sign
			for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
			//key starts 2 after the =
			bufferPt += 2;
			for (i = 0; i < 16; i ++){
				sscanf(bufferPt, "%2hhx", &key[i]);
				bufferPt += 2;
			}
			//next line
			fgets(buffer, 128, fp);
			//parse IV
			//find = sign
			for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
			//IV starts 2 after the =
			bufferPt += 2;
			for (i = 0; i < 16; i ++){
				sscanf(bufferPt, "%2hhx", &iv[i]);
				bufferPt += 2;
			}
			//next line
			fgets(buffer, 128, fp);
			//parse plaintext
			//find = sign
			for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
			//plaintext starts 2 after the =
			bufferPt += 2;
			for (i = 0; i < 16; i ++){
				sscanf(bufferPt, "%2hhx", &plaintext[i]);
				bufferPt += 2;
			}
			//next line
			fgets(buffer, 128, fp);
			//parse ciphertext
			//find = sign
			for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
			//ciphertext starts 2 after the =
			bufferPt += 2;
			for (i = 0; i < 16; i ++){
				sscanf(bufferPt, "%2hhx", &ciphertext[i]);
				bufferPt += 2;
			}
			printf("Stimulus: Count = %u\n", count);
			printf("Stimulus: Key = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", key[i]);
			}
			printf("\n");
			printf("Stimulus: IV = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", iv[i]);
			}
			printf("\n");
			printf("Stimulus: Plaintext = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", plaintext[i]);
			}
			printf("\n");
			printf("Stimulus: Ciphertext = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", ciphertext[i]);
			}
			printf("\n");

			//send data out
			blockOut.send(&plaintext[0], sizeof(unsigned char) * 16);
		}
	}
};
