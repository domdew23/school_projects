#ifndef PC_H
#define PC_H
using namespace std;

class PC : public Creature{
	public:
		PC(int id, Room* rooms[], int num_rooms, int loc, string type)
		: Creature(id, rooms, num_rooms, loc, type) {};
		void react(string action, bool this_creat, int* respect);
};

#endif
