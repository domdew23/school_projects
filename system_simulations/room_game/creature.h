#ifndef CREATURE_H
#define CREATURE_H
#include <iostream>
#include <vector>
#include <string>
#include "room.h"
using namespace std;

class Room;
class Creature{
	public:
		Creature(int id, Room* rooms[], int num_rooms, int location, string type);
		void look();
		int set_current_room(Room* r[], int num_rooms, int location);
		Room* get_current_room();
		int get_id();
		string get_type();
		void clean_room();
		void dirty_room();
		void leave_room();
	protected:
		int id;
		string type;
		Room* current_room;
	private:
};

#endif