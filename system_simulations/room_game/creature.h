#ifndef CREATURE_H
#define CREATURE_H
#include <iostream>
#include <vector>
#include "room.h"
using namespace std;

class Room;
class Creature{
	public:
		Creature();
		void look();
		void set_id(int i);
		int set_current_room(Room* r[], int num_rooms, int location);
		Room* get_current_room();
		int get_id();
	protected:
		int id;
		Room* current_room;
	private:
};

#endif