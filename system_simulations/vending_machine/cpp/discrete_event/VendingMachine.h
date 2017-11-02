#ifndef VENDING_MACHINE_H
#define VENDING_MACHINE_H
#include "Time.h"
using namespace std;

class VendingMachine {
	public:
		VendingMachine(int init_quarters, int init_nickels, int init_dimes);
		void delta_external(char x, double e);
		void delta_internal();
		void delta_confluent(char x);
		int * lambda();
		int time_advance();
		double get_real();
	private:
		int* dispense_change()
		int quarters;
		int nickels;
		int dimes;
		int value;
		Time* current;

};


#endif
