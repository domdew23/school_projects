#include <iostream>
#include "VendingMachine.h"
#include "Time.h"

VendingMachine::VendingMachine(int init_quarters, int init_nickels, int init_dimes){
	this->quarters = init_quarters;
	this->nickels = init_nickels;
	this->dimes = init_dimes;
	this->value = 0;
	this->current = new Time(0, 0);
}

void VendingMachine::delta_external(char x){
	switch(x){
		case 'q': quarters++; value += 25; break;
		case 'd': dimes++; value += 10; break;
		case 'n': nickels++; value += 5; break;
		default: break;
	}
}

void VendingMachine::delta_internal(){
	if (value >= 100){
		value %= 100;
	}
	
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

void VendingMachine::delta_confluent(char x){
	delta_internal();
	delta_external(x);
}

int* VendingMachine::lambda(){
	int coffee = value/100;
	int* change = dispense_change();
	int* y = new int[4]{coffee, change[0], change[1], change[2]};
	return y;
}

int VendingMachine::time_advance(){
	if (value > 0){
		return 2;
	} else {
		return 99999999;
	}
}

int* VendingMachine::dispense_change(){
	int q_count=0, n_count=0, d_count=0;
	int tmp_value = value % 100;
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

void VendingMachine::print_state(){
	cout << "Quarters: " << quarters << " || Nickels: " << nickels << 
	" || Dimes: " << dimes << " || Value: " << value << endl;
}

double VendingMachine::get_real(){
	return current->get_real();
}
