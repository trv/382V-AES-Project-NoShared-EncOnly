#define DEBUG_STIM 1
#define DEBUG_STIM_IV 0
#define CBC_VECTORS	"vectors/CBCMCT128.rsp"
#define ECB_VECTORS	"vectors/ECBMCT128.rsp"
#define ENCRYPT_LIST "encrypt.txt"
#define DECRYPT_LIST "decrypt.txt"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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
		int mcIndexI, mcIndexJ;
		unsigned char key[16], plainText[16], cipherText[16], CT[16];
		fp = fopen(ECB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", ECB_VECTORS);
		} else {
			printf("Beginning ECB Monte Carlo Encryption test\n");
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

	void runECBMCTDec(){
		FILE *fp;
		char buffer[128];
		char * bufferPt;
		int count;
		unsigned long length;
		unsigned char mode;
		int i;
		int mcIndexI, mcIndexJ;
		unsigned char key[16], plainText[16], cipherText[16], PT[16];
		fp = fopen(ECB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", ECB_VECTORS);
		} else {
			printf("Beginning ECB Monte Carlo Decryption test\n");
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

	void encryptFile(FILE * ifp, FILE * ofp, unsigned char * key){
		int charCount = 0;
		int i;
		int end = 0;
		unsigned char buffer[16];
		unsigned long fileLength = 0;
		unsigned long h;
		unsigned char mode;

		//get length of the file
		fseek(ifp, 0L, SEEK_END);
		fileLength = ftell(ifp);
		rewind(ifp);
		if (fileLength % 16 == 0) fileLength = fileLength / 16;
		else fileLength = (fileLength / 16) + 1;

		mode = 1;
		qModeOut.send(&mode, sizeof(unsigned char));
		
		qKeyOut.send(&key[0], sizeof(unsigned char) * 16);
		qLengthOut.send(&fileLength, sizeof(unsigned long));

		for (h = 0; h < fileLength; h++){
			//get a block from the file
			for(i = 0; i < 16; i++){
				buffer[i] = fgetc(ifp);
				if (buffer[i] == EOF){
					end = 0;
					//fill remaining with 0
					for(; i < 16; i++){
						buffer[i] = 0;
					}
					break;
				}
			}
			//send the block
			qBlockOut.send(&buffer[0], sizeof(unsigned char) * 16);
			//receive the encrypted block
			qMonFeedback.receive(&buffer[0], sizeof(unsigned char) * 16);
			//write out the encrypted block
			for (i = 0; i < 16; i++){
				fprintf(ofp, "%hhu", buffer[i]);
			}
		}
	}

	void decryptFile(FILE * ifp, FILE * ofp, unsigned char * key){
		
	}

	void main (void){
		FILE *  fp;
		FILE * ifp;
		FILE * ofp;
		char buffer1[128];
		unsigned char buffer2[128];
		
		runECBMCTEnc();
		runECBMCTDec();
		/*
		//get files to encrypt
		fp = fopen(ENCRYPT_LIST, "r");
		if (fp == NULL){
			printf("Could not open encryption list");
			exit(1);
		}
		//get file name and encryption key
		fscanf(fp, "%s %s", buffer1, buffer2);
#if DEBUG_STIM
		printf("File to encrypt: %s\n\twith key: %s\n", buffer1, buffer2);
#endif
		ifp = fopen(buffer1, "rb");
		strcat(buffer1, ".enc");
		ofp = fopen(buffer1, "wb");

		if (ifp == NULL){
				printf("Cound not open file to encrypt\n");
		}
		if (ofp == NULL){
				printf("Count not open file to write encrypted data\n");
		}

		encryptFile(ifp, ofp, buffer2);

		fclose(fp);
		fclose(ifp);
		fclose(ofp);
		*/
		exit(0);
	}
};
