#ifndef ANIMAL_H
#define ANIMAL_H
#include "creature.h"
using namespace std;

class Animal: public Creature{
	public:
		Animal(int id, Room* rooms[], int num_rooms, int loc, string type) 
		: Creature(id, rooms, num_rooms, loc, type) {};
		bool react(string action, bool this_creat, int* respect);
};

#endif