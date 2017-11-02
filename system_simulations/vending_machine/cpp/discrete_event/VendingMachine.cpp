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

double VendingMachine::get_real(){
	return current->get_real();
}
