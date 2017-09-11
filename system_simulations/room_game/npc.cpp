#include <iostream>
#include <string>
#include <vector>
#include "creature.h"
#include "room.h"
#include "npc.h"
using namespace std;

void NPC::react(string action, bool this_creat, int* respect){
	int multiplier = 1;
	string txt = "";
	if (this_creat){
		txt = " a lot";
		multiplier = 3;
	}

	if (action == "dirty"){
		*respect += multiplier;
		cout << id << " smiles" << txt << ". Respect is now " << *respect << endl;
	} else if (action == "clean"){
		*respect -= multiplier;
		cout << id << " grumbles" << txt << ". Respect is now " << *respect << endl;
		if (current_room->get_state() == 0){
			check_status(respect);
		}
	} else {
		//error
	}
}
