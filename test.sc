import "c_queue";
import "stimulus";
import "byteSub128";
import "shiftRow128";
import "mixColumns128";
import "monitor";
import "addRoundKey128";
import "keySched128";

behavior Main (){
	const unsigned long queueSize = 1024;
	c_queue queueIn(queueSize), queueOut(queueSize), queueKey(queueSize), queueExpandedKey(queueSize);
	c_queue queueSubShift(queueSize), queueShiftMix(queueSize), queueMixAdd(queueSize);

	stimulus stim_inst(queueIn, queueKey);
	monitor monitor_inst(queueOut);
	byteSub128 byteSub_inst(queueIn, queueSubShift);
	shiftRow128 shift_inst(queueSubShift, queueShiftMix);
	MixColumns128 mix_inst(queueShiftMix, queueMixAdd);
	addRoundKey128 add_inst(queueMixAdd, queueExpandedKey, queueOut);
	keySched128 key_inst(queueKey, queueExpandedKey);

	int main (void) {
		par{
			stim_inst;
			byteSub_inst;
			shift_inst;
			mix_inst;
			add_inst;
			monitor_inst;
		}
		return 0;
	}
};
