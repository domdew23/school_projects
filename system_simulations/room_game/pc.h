#ifndef PC_H
#define PC_H
#include <string>
#include "creature.h"

class PC : public Creature{
	public:
		PC(int id, Room* rooms[], int num_rooms, int loc, string type)
		: Creature(id, rooms, num_rooms, loc, type) {};
};

#endif