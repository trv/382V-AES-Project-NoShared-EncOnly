#define DEBUG_STIM 0
#define DEBUG_STIM_IV 0
#define TEST_LENGTH 1
#define CBC_VECTORS	"vectors/CBCMCT128.rsp"
#define ECB_VECTORS	"vectors/ECBMCT128.rsp"
#include <stdio.h>
#include <stdlib.h>

import "i_receiver";
import "i_sender";

behavior stimulus(i_sender qBlockOut, i_sender qKeyOut, i_sender qModeOut, i_sender qLengthOut, i_sender qIVOut, i_receiver qMonFeedback) {

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

	void runECBMCTEnc(void){
		FILE *fp;
		char buffer[128];
		char * bufferPt;
		int count;
		unsigned long length;
		unsigned char mode;
		int i;
		int countIndex, mcIndexI, mcIndexJ;
		unsigned char key[16], plainText[16], cipherText[16], CT[16];
		fp = fopen(ECB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", ECB_VECTORS);
		} else {
			printf("Beginning ECB Monte Carlo Encryption test\n");
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
#if DEBUG_STIM
				printf("Stimulus: Count = %u\n", count);
				printf("Stimulus: Key = ");
				printBlockLn(key, 16);
				printf("Stimulus: Plaintext = ");
				printBlockLn(plainText, 16);
				printf("Stimulus: Ciphertext = ");
				printBlockLn(cipherText, 16);
#endif				
				mode = 1;
				length = 1;
				
				printf("[ENCRYPT]\n\n");
				for (mcIndexI = 0; mcIndexI < 100; mcIndexI++){
					printf("COUNT = %u\n", mcIndexI);
					printf("KEY = ");
					printBlockLn(key, 16);
					printf("PLAINTEXT = ");
					printBlockLn(plainText, 16);
					for (mcIndexJ = 0; mcIndexJ < 1000; mcIndexJ++){
#if DEBUG_STIM
						printf("Stimulus:Iblock (j=%u) = ", mcIndexJ);
						printBlockLn(plainText, 16);
#endif
						qModeOut.send(&mode, sizeof(unsigned char));
#if DEBUG_STIM
						printf("Stimulus: sent mode.\n");
#endif
						qKeyOut.send(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: sent key.\n");
#endif
						qBlockOut.send(&plainText[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: sent plaintext.\n");
#endif
						qLengthOut.send(&length, sizeof(unsigned long));
#if DEBUG_STIM
						printf("Stimulus: sent length.\n");
#endif
						qMonFeedback.receive(&CT[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: received ciphertext from monitor.\n");
#endif
						//next plaintext is current ciphertext
						for (i = 0; i < 16; i++){plainText[i] = CT[i];}
					}
					//output CT
					printf("CIPHERTEXT = ");
					printBlockLn(CT, 16);
					printf("\n");
					//key = key xor CT
					for (i = 0; i < 16; i++){key[i] = key[i] ^ CT[i];}
				}
			}
		}
	}

	void runECBMCTDec(){
		FILE *fp;
		char buffer[128];
		char * bufferPt;
		int count;
		unsigned long length;
		unsigned char mode;
		int i;
		int countIndex, mcIndexI, mcIndexJ;
		unsigned char key[16], plainText[16], cipherText[16], CT[16], PT[16];
		fp = fopen(ECB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", ECB_VECTORS);
		} else {
			printf("Beginning ECB Monte Carlo Decryption test\n");
			for (countIndex = 0; countIndex < TEST_LENGTH; countIndex++){
				//advance to the "[DECRYPT]" in the file
				for (fgets(buffer, 128, fp); buffer[0] != '[' || buffer[1] != 'D' ; fgets(buffer, 128, fp)) {}
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
				//parse cipherText
				//find = sign
				for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
				//ciphertext starts 2 after the =
				bufferPt += 2;
				for (i = 0; i < 16; i ++){
					sscanf(bufferPt, "%2hhx", &cipherText[i]);
					bufferPt += 2;
				}
				//next line
				fgets(buffer, 128, fp);
				//parse plainText
				//find = sign
				for(bufferPt = &buffer[0]; *bufferPt != '='; bufferPt++){}
				//cipherText starts 2 after the =
				bufferPt += 2;
				for (i = 0; i < 16; i ++){
					sscanf(bufferPt, "%2hhx", &plainText[i]);
					bufferPt += 2;
				}
#if DEBUG_STIM
				printf("Stimulus: Count = %u\n", count);
				printf("Stimulus: Key = ");
				printBlockLn(key, 16);
				printf("Stimulus: Ciphertext = ");
				printBlockLn(cipherText, 16);
				printf("Stimulus: Plaintext = ");
				printBlockLn(plainText, 16);
#endif				
				mode = 2;
				length = 1;
				
				printf("[DECRYPT]\n\n");
				for (mcIndexI = 0; mcIndexI < 100; mcIndexI++){
					printf("COUNT = %u\n", mcIndexI);
					printf("KEY = ");
					printBlockLn(key, 16);
					printf("CIPHERTEXT = ");
					printBlockLn(cipherText, 16);
					for (mcIndexJ = 0; mcIndexJ < 1000; mcIndexJ++){
#if DEBUG_STIM
						printf("Stimulus:Iblock (j=%u) = ", mcIndexJ);
						printBlockLn(cipherText, 16);
#endif
						qModeOut.send(&mode, sizeof(unsigned char));
#if DEBUG_STIM
						printf("Stimulus: sent mode.\n");
#endif
						qKeyOut.send(&key[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: sent key.\n");
#endif
						qBlockOut.send(&cipherText[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: sent ciphertext.\n");
#endif
						qLengthOut.send(&length, sizeof(unsigned long));
#if DEBUG_STIM
						printf("Stimulus: sent length.\n");
#endif
						qMonFeedback.receive(&PT[0], sizeof(unsigned char) * 16);
#if DEBUG_STIM
						printf("Stimulus: received plaintext from monitor.\n");
#endif
						//next plaintext is current ciphertext
						for (i = 0; i < 16; i++){cipherText[i] = PT[i];}
					}
					//output CT
					printf("PLAINTEXT = ");
					printBlockLn(PT, 16);
					printf("\n");
					//key = key xor CT
					for (i = 0; i < 16; i++){key[i] = key[i] ^ PT[i];}
				}
			}
		}
	}

	void main (void){
		runECBMCTEnc();
		runECBMCTDec();
								
		/*
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
		*/

		/*
		//send out decryption data
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
