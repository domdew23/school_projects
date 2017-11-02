#ifndef SCHEDULER_H
#define SCHEDULER_H
#include "VendingMachine.h"
using namespace std;

class Scheduler {
	public:
		Scheduler(VendingMachine* VM);
		void schedule(char x, double e);
	private:
		void print_output(int* y);
		VendingMachine* VM;
};

#endif
