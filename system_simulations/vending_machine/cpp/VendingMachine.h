#ifndef VENDINGMACHINE_H
#define VENDINGMACHINE_H
using namespace std;

class VendingMachine{
	public:
		VendingMachine(int init_quarters, int init_nickels, int init_dimes);
<<<<<<< HEAD
		int[] lambda();
=======
		int* lambda();
>>>>>>> 5893852dccfc81e4b659240b7811415b9bbcc40e
		void delta(int argc, char* args);
		void print_state();
		int* dispense_change();
		void decrement_coins();	
	private:
		int quarters;
		int nickels;
		int dimes;
		int value;
		bool cancel;
};

#endif


