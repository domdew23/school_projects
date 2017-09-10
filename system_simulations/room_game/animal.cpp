#include <iostream>
#include <string>
#include <vector>
#include "creature.h"
#include "room.h"
#include "animal.h"
using namespace std;

void Animal::react() {
	cout << "animal reacting..." << endl;
	// must change room's state before reacting
	string action;
	if (action == "clean"){
		//lickface, respect++
	} else if (action == "dirty"){
		//growl, respect--
	} else {
		//error
	}
}
