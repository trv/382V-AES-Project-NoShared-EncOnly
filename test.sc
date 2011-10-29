import "c_queue";
import "stimulus";
import "byteSub128";
import "monitor";

behavior Main (){
	const unsigned long queueSize = 1024;
	c_queue queue(queueSize), queueOut(queueSize);
	stimulus stim_inst(queue);
	monitor monitor_inst(queueOut);
	byteSub128 byteSub_inst(queue, queueOut);
	int main (void) {
		par{
			stim_inst;
			byteSub_inst;
			monitor_inst;
		}
		return 0;
	}
};
