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
/*
	void runECBMCTEnc(void){
		FILE *fp;
		char buffer[128];
		char * bufferPt;
		int count;
		unsigned long length;
		int i;
		int mcIndexI, mcIndexJ;
		unsigned char key[16], plainText[16], cipherText[16], CT[16];
		fp = fopen(ECB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", ECB_VECTORS);
		} else {
#if DEBUG_STIM
			printf("Beginning ECB Monte Carlo Encryption test\n");
#endif
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
					//qModeOut.send(&mode, sizeof(unsigned char));
#if DEBUG_STIM
					//printf("Stimulus: sent mode.\n");
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
		//unsigned char mode;
		int i;
		int mcIndexI, mcIndexJ;
		unsigned char key[16], plainText[16], cipherText[16], PT[16];
		fp = fopen(ECB_VECTORS, "r");
		if (fp == NULL){
			printf("Cannot open %s\n", ECB_VECTORS);
		} else {
#if DEBUG_STIM
			printf("Beginning ECB Monte Carlo Decryption test\n");
#endif
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
					//qModeOut.send(&mode, sizeof(unsigned char));
#if DEBUG_STIM
					//printf("Stimulus: sent mode.\n");
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
		//unsigned char mode;

		//get length of the file
		fseek(ifp, 0L, SEEK_END);
		fileLength = ftell(ifp);
		rewind(ifp);
		if (fileLength % 16 == 0) fileLength = fileLength / 16;
		else fileLength = (fileLength / 16) + 1;

		mode = 1;
		//qModeOut.send(&mode, sizeof(unsigned char));
		
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

*/
	void main (void){
		/*FILE *  fp;
		FILE * ifp;
		FILE * ofp;
		char buffer1[128];
		unsigned char buffer2[128];
    */
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

      // done with encrypt test, so start on decrypt mc
      input_key[ 0] = 0x0c;
      input_key[ 1] = 0x60;
      input_key[ 2] = 0xe7;
      input_key[ 3] = 0xbf;
      input_key[ 4] = 0x20;
      input_key[ 5] = 0xad;
      input_key[ 6] = 0xa9;
      input_key[ 7] = 0xba;
      input_key[ 8] = 0xa9;
      input_key[ 9] = 0xe1;
      input_key[10] = 0xdd;
      input_key[11] = 0xf0;
      input_key[12] = 0xd1;
      input_key[13] = 0x54;
      input_key[14] = 0x07;
      input_key[15] = 0x26;

      input_block[ 0] = 0xb0;
      input_block[ 1] = 0x8a;
      input_block[ 2] = 0x29;
      input_block[ 3] = 0xb1;
      input_block[ 4] = 0x1a;
      input_block[ 5] = 0x50;
      input_block[ 6] = 0x0e;
      input_block[ 7] = 0xa3;
      input_block[ 8] = 0xac;
      input_block[ 9] = 0xa4;
      input_block[10] = 0x2c;
      input_block[11] = 0x36;
      input_block[12] = 0x67;
      input_block[13] = 0x5b;
      input_block[14] = 0x97;
      input_block[15] = 0x85;

      mode = 2; // ECB decrypt MC test
    }

    if (iter == 2000) {
      printf("Monte Carlo Decrypt Test: ");
      printBlock(output_block, 16);
      printf("\n");
      exit(0);
    }

		iter++;

		//runECBMCTEnc();
		//runECBMCTDec();
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
		//exit(0);
	}
};
