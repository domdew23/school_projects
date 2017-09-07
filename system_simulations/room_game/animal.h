#ifndef ANIMAL_H
#define ANIMAL_H
#include <string>
#include "creature.h"

class Animal: public Creature{
	public:
		Animal(int id, Room* rooms[], int num_rooms, int loc, string type) 
		: Creature(id, rooms, num_rooms, loc, type) {};
		void react(string action);
};

#endif