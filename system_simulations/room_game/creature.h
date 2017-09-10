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
		void clean();
		void dirty();
		void leave(int i, string txt);
		virtual void react() = 0;
		~Creature();
	protected:
		int id;
		string type;
		Room* current_room;
	private:
};

#endif
