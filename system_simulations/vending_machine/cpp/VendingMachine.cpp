#include <iostream>
#include <string>
#include "VendingMachine.h"

using namespace std;

VendingMachine::VendingMachine(int init_quarters, int init_nickels, int init_dimes){
	this->quarters = init_quarters;
	this->nickels = init_nickels;
	this->dimes = init_dimes;
}

string VendingMachine::lambda(){

}

void VendingMachine::delta(int args){
}

void VendingMachine::print_state(){
	cout << "Quarters: " << quarters << " || Nickels: " << nickels << " || Dimes: " << dimes << endl;
}

void VendingMachine::dispense_change(){
}
