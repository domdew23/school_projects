#include <iostream>
#include <string>
#include "VendingMachine.h"

using namespace std;

VendingMachine::VendingMachine(int init_quarters, int init_nickels, int init_dimes){
	this->quarters = init_quarters;
	this->nickels = init_nickels;
	this->dimes = init_dimes;
	this->value = 0;
	this->cancel = false;
}

string VendingMachine::lambda(){
	// cant change the value
	// outputs change, coffee, nothing
	return "";
}

void VendingMachine::delta(int argc, char* args){
	cancel = false;
	for (int i = 0; i < argc; i++){
		switch (args[i]){
			case 'q': quarters++; value += 25; break;
			case 'n': nickels++;  value += 5;  break;
			case 'd': dimes++; 	  value += 10; break;
			case 'c': cancel=true; break;
			case 'w': return;
			default: cout << "Invalid input"; break;
		}
	}
}

void VendingMachine::print_state(){
	cout << "Quarters: " << quarters << " || Nickels: " << nickels << " || Dimes: " << dimes << " || Value: " << value << " || Cancel: " << cancel << endl;
}

void VendingMachine::dispense_change(){
}
