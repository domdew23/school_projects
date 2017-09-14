#ifndef ROOM_H
#define ROOM_H
using namespace std;

class Creature;
class Room {
	public:
		Room(int id, int state, int neighbors_ids[]);
		void init_neighbors(Room* rooms[]);
		bool add_creature(Creature* c);
		bool remove_creature(Creature* c);
		int get_id();
		int get_state();
		void print_description();
		bool is_full();
		int get_size();
		vector<Creature*> get_creatures();
		bool contains(Creature* c);
		Room** get_neighbors();
		void change_state(string change, Creature* creature, int* respect, bool forced=false);
		~Room();

	private:
		// [0] = north, [1] = south, [2] = east, [3] = west
		int id, state, size;
		Room* neighbors[4];
		int neighbor_ids[4];
		vector<Creature*> creatures;	
};

#endif