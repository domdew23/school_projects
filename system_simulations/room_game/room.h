#ifndef ROOM_H
#define ROOM_H
#include <iostream>
#include <vector>
#include "creature.h"
using namespace std;

class Creature;
class Room {
	public:
		Room(int id, int state, int neighbors_ids[]);
		void init_neighbors(Room* rooms[]);
		void add_creature(Creature* c);
		void set_north(Room* r);
		void set_south(Room* r);
		void set_east(Room* r);
		void set_west(Room* r);
		Room get_north();
		Room get_south();
		Room get_east();
		Room get_west();
		int get_id();
		int get_state();
		void print_neighbors();
		void print_description();

	private:
		// [0] = north, [1] = south, [2] = east, [3] = west
		int id, state;
		Room* neighbors[4];
		int neighbor_ids[4];
		vector<Creature*> creatures;	
};

#endif