#ifndef VENDINGMACHINE_H
#define VENDINGMACHINE_H
using namespace std;

class VendingMachine{
	public:
		VendingMachine(int init_quarters, int init_nickels, int init_dimes);
		string lambda();
		void delta(int argc, char* args);
		void print_state();
		void dispense_change();	
	private:
		int quarters;
		int nickels;
		int dimes;
		int value;
		bool cancel;
};

#endif


