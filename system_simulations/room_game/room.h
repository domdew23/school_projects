#include <iostream>
#include <fstream>
using namespace std;

class Room {
	public:
		Room();
		Room(int id, int state, int neighbors_ids[]);
		void init_neighbors(Room* rooms[]);
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

	private:
		int id, state;
		//Room* north, south, east, west;
		// [0] = north, [1] = south, [2] = east, [3] = west
		Room* neighbors[4];
		int neighbor_ids[4];	
		//vector<Room*> neighbors;
};