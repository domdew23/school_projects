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

int* VendingMachine::lambda(){
	int coffee=0, nothing=0;

	if (cancel){
		int* change = dispense_change();
		int* y = new int[5]{coffee, change[0], change[1], change[2], nothing};
		return y;
	} else if (value >= 100){
		coffee = value / 100;
	}

	if (!coffee){
		nothing = 1;
	}

	int* y = new int[5]{coffee, 0, 0, 0, nothing};
	return y;
}

void VendingMachine::delta(int argc, char* args){
	if (cancel){
		cancel = false;
		if (value > 0){
			change_state();
		}
	} else if (value >= 100){
		value %= 100;
	}

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
	cout << "Quarters: " << quarters << " || Nickels: " << nickels << 
	" || Dimes: " << dimes << " || Value: " << value << " || Cancel: " << cancel << endl;
}

void VendingMachine::change_state(){
	int* change = dispense_change();
	for (int i = 0; i < change[0]; i++){
		value -= 25;
		quarters--;
	}

	for (int i = 0; i < change[1]; i++){
		value -= 5;
		nickels--;
	}

	for (int i = 0; i < change[2]; i++){
		value -= 10;
		dimes--;
	}
}

int* VendingMachine::dispense_change(){
	int q_count=0, n_count=0, d_count=0;
	int tmp_value = value;
	while (tmp_value != 0){
		while (tmp_value >= 25){
			if (quarters > 0){
				if (tmp_value < 50 && tmp_value % 10 == 0){
					if (dimes > 0){
						break;
					}
				}
				q_count++;
				tmp_value -= 25;
			} else {
				break;
			}
		}
		while (tmp_value >= 10){
			if (dimes > 0){
				d_count++;
				tmp_value -= 10;
			} else {
				break;
			}
		}
		while (tmp_value >= 5){
			if (nickels > 0){
				n_count++;
				tmp_value -= 5;
			} else {
				break;
			}
		}
		if (tmp_value != 0){
			cout << "OUT OF SUFFICIENT CHANGE" << endl;
			break;
		}
	}

	int* pointer = new int[3]{q_count, n_count, d_count};
	return pointer;
}
