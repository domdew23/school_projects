#ifndef NPC_H
#define NPC_H
#include "creature.h"
using namespace std;

class Creature;
class NPC : public Creature{
	public:
		NPC(int id, Room* rooms[], int num_rooms, int loc, string type)
		: Creature(id, rooms, num_rooms, loc, type) {};\
		void react();
};

#endif