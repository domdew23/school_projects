#ifndef CREATURE_H
#define CREATURE_H
using namespace std;

class Room;
class Creature{
	public:
		Creature(int id, Room** rooms, int num_rooms, int location, string type);
		void look();
		int set_current_room(Room* r);
		Room* get_current_room();
		int get_id();
		string get_type();
		void clean(Creature* creature, int* respect);
		void dirty(Creature* creature, int* respect);
		void leave(int i, string txt);
		virtual void react(string action, bool this_creat, int* respect) = 0;
		void check_status();
		~Creature();
	protected:
		int id;
		string type;
		Room* current_room;
	private:
};

#endif
