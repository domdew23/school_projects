#ifndef ANIMAL_H
#define ANIMAL_H
#include "creature.h"
using namespace std;

class Creature;
class Animal: public Creature{
	public:
		Animal(int id, Room* rooms[], int num_rooms, int loc, string type) 
		: Creature(id, rooms, num_rooms, loc, type) {};
		void react();
};

#endif