#define DEBUG_STIM 0
#define TEST_LENGTH 2
#define BYTE_SUB_VECTORS	"vectors/CBCGFSbox128.rsp"
#include <stdio.h>
#include <stdlib.h>

//Channels
import "c_queue";

behavior stimulus(i_sender encBlockOut, i_sender decBlockOut, i_sender keyEncOut, i_sender keyDecOut, i_receiver encBlockIn, i_receiver decBlockIn) {
	void main (void){
		FILE *fp;
		char buffer[128];
		char * bufferPt;
		int count;
		int i;
		int j;
		unsigned char key[16], iv[16], plaintext[16], ciphertext[16], temp[16];

		fp = fopen(BYTE_SUB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", BYTE_SUB_VECTORS);
			exit(1);
		}
		//advance to the first "COUNT" in the file
		for (j = 0; j < TEST_LENGTH; j++){
			for (fgets(buffer, 128, fp); buffer[0] != 'C'; fgets(buffer, 128, fp)) {}
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

			//send out encryption data
			encBlockOut.send(&plaintext[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
			printf("Stimulus: Sent data to encrypt\n");
#endif
			keyEncOut.send(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
			printf("Stimulus: Sent key to encrypt\n");
#endif
			//get back encrypted data
			encBlockIn.receive(&temp[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
			printf("Stimulus: Received encrypted data\n");
#endif
			//send out dectyption data
			decBlockOut.send(&temp[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
			printf("Stimulus: Sent data to decrypt\n");
#endif
			keyDecOut.send(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
			printf("Stimulus: Sent key to decrypt\n");
#endif
			//get back decrypted data
			decBlockIn.receive(&temp[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
			printf("Stimulus: Received decrypted data\n");
#endif
		}
		exit(0);
	}
};
