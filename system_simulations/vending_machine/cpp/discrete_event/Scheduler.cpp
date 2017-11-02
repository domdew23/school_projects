#include <iostream>
#include "Scheduler.h"
#include "VendingMachine.h"
using namespace std;

Scheduler::Scheduler(VendingMachine* VM){
	this->VM = VM;
}

void Scheduler::schedule(char x, double e){
	int ta = VM->time_advance();
	Time* next;
	if (e == 0){
		next = new Time(ta, 0);
		print_output(VM->lambda());
		VM->delta_internal();
		cout << "Executing delta_internal at " + VM->get_real(); << endl;
		schedule(x, e-ta);
		return;
	} else if (e == ta){
		next = new Time(ta, 0);
		print_output(VM->lambda());
		cout << "Executing delta_confluent at ";
		VM->delta_confluent();
	} else {
		next = new Time(e, 0);
		cout << "Executing delta_external at ";
		VM->delta_external(x);
	}
	
	VM->current = VM->current->advance(next);
	cout << VM->get_real() << endl;
}

void Scheduler::print_output(int* y){
	
}

