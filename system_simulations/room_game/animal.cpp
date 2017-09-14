#include <iostream>
#include <string>
#include <vector>
#include "creature.h"
#include "room.h"
#include "animal.h"
using namespace std;

bool Animal::react(string action, bool this_creat, int* respect) {
	int multiplier = 1;
	happy = true;
	string txt = "";	
	if (this_creat){
		txt = " a lot";
		multiplier = 3;
	}

	if (action == "clean"){
		*respect += multiplier;
		cout << id << " licks your face" << txt << ". Respect is now " << *respect << endl;
		return false;
	} else if (action == "dirty"){
		*respect -= multiplier;
		cout << id << " growls" << txt << ". Respect is now " << *respect << endl;
		if (current_room->get_state() == 2){
			return true;
		}
		return false;
	} else {
		//error
	}
}
