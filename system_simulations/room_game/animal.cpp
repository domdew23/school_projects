#include <iostream>
#include <string>
#include "animal.h"
#include "creature.h"
using namespace std;

void Animal::react(string action){
	// must change room's state before reacting
	if (action == "clean"){
		//lickface, respect++
	} else if (action == "dirty"){
		//growl, respect--
	} else {
		//error
	}
}