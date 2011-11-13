#define DEBUG_STIM 1
#define DEBUG_STIM_IV 0
#define TEST_LENGTH 1
#define CBC_VECTORS	"vectors/CBCMCT128.rsp"
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
		int countIndex, mcIndexI, mcIndexJ;
		unsigned char key[16], iv[16], plainText[16], cipherText[16], CT[16], chainVal[16], inputBlock[16], prevCT[16], temp[16];

		fp = fopen(CBC_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", CBC_VECTORS);
			exit(1);
		}
		//Cipher Block Chaining Test
		printf("Beginning CBC Monte Carlo test\n");
		for (countIndex = 0; countIndex < TEST_LENGTH; countIndex++){
			//advance to the next "C" in the file
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
			//parse plainText
			//find = sign
			for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
			//plainText starts 2 after the =
			bufferPt += 2;
			for (i = 0; i < 16; i ++){
				sscanf(bufferPt, "%2hhx", &plainText[i]);
				bufferPt += 2;
			}
			//next line
			fgets(buffer, 128, fp);
			//parse cipherText
			//find = sign
			for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
			//cipherText starts 2 after the =
			bufferPt += 2;
			for (i = 0; i < 16; i ++){
				sscanf(bufferPt, "%2hhx", &cipherText[i]);
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
				printf("%02hhx", plainText[i]);
			}
			printf("\n");
			printf("Stimulus: Ciphertext = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", cipherText[i]);
			}
			printf("\n");
#if DEBUG_STIM_IV
			//decode the IV for debug
			decBlockOut.send(&iv[0], sizeof(unsigned char) * 16);
			keyDecOut.send(&key[0], sizeof(unsigned char) * 16);
			decBlockIn.receive(&temp[0], sizeof(unsigned char) * 16);
			printf("Stimulus: decoded IV = ");
			for (i = 0; i < 16; i++){
				printf("%02hhx", temp[i]);
			}
			printf("\n");
#endif
			for (mcIndexI = 0; mcIndexI < 100; mcIndexI++){
				for (mcIndexJ = 0; mcIndexJ < 1000; mcIndexJ++){
					if (mcIndexJ == 0) {
						//CT(j) = AES(key(i), IV(i), PT(i))
						//plaintext = plaintext xor init vector
						for (i = 0; i < 16; i++){
							inputBlock[i] = iv[i] ^ plainText[i];
						}
#if DEBUG_STIM
						printf("Stimulus: Ptext (j=%u) = ", mcIndexJ);
						for (i = 0; i < 16; i++){
							printf("%02hhx", plainText[i]);
						}
						printf("\n");
						printf("Stimulus:Iblock (j=%u) = ", mcIndexJ);
						for (i = 0; i < 16; i++){
							printf("%02hhx", inputBlock[i]);
						}
						printf("\n");
#endif
						encBlockOut.send(&inputBlock[0], sizeof(unsigned char) * 16);
						keyEncOut.send(&key[0], sizeof(unsigned char) * 16);
						encBlockIn.receive(&CT[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: Ctext (j=%u) = ", mcIndexJ);
						for (i = 0; i < 16; i++){
							printf("%02hhx", CT[i]);
						}
						printf("\n");
#endif
						//PT(j+1) = IV(i)
						for(i = 0; i<16; i++){
							plainText[i] = iv[i];
						}
					} else {
						//CT(j) = AES(Key(i), PT(i)
						//plaintext = plaintext xor prev ciphertext
						for (i = 0; i < 16; i++){
							inputBlock[i] = plainText[i] ^ CT[i];
						}
#if DEBUG_STIM
						printf("Stimulus: Ptext (j=%u) = ", mcIndexJ);
						for (i = 0; i < 16; i++){
							printf("%02hhx", plainText[i]);
						}
						printf("\n");
						printf("Stimulus:Iblock (j=%u) = ", mcIndexJ);
						for (i = 0; i < 16; i++){
							printf("%02hhx", inputBlock[i]);
						}
						printf("\n");
#endif
						encBlockOut.send(&inputBlock[0], sizeof(unsigned char) * 16);
						keyEncOut.send(&key[0], sizeof(unsigned char) * 16);
						encBlockIn.receive(&CT[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: Ctext (j=%u) = ", mcIndexJ);
						for (i = 0; i < 16; i++){
							printf("%02hhx", CT[i]);
						}
						printf("\n");
#endif
						//PT(i+1) = CT(j-1)
						for (i = 0; i < 16; i++){
							plainText[i] = prevCT[i];
							prevCT[i] = CT[i];
						}
					}
				}
				//TODO key manipulation
			}
		}
		/*
		//send out dectyption data
		decBlockOut.send(&CT[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
		printf("Stimulus: Sent data to decrypt\n");
#endif
		keyDecOut.send(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
		printf("Stimulus: Sent key to decrypt\n");
#endif
		//get back decrypted data
		decBlockIn.receive(&CT[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
		printf("Stimulus: Received decrypted data\n");
#endif
		*/
		exit(0);
	}
};
