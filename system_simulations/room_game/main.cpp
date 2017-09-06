#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include "room.h"
#include "creature.h"
#include "animal.h"
using namespace std;

// rooms connected with doors, contain creatures
// every room has at most 4 doors (North South East West) which stay in same place
// every room has at most 10 creatures including PC - creatures will be denied with "Room Full"
// creatures move between rooms (enter or leave)
// when a creature leaves one room it has to enter antoher
// 3 types of creatures: Player(PC), non-player(NPC), animals
// each creature has 'look' command which shows name, state, description of room as well as creatures in room
// room states: dirty, half-dirty, clean
// creatures have preferences on room cleanliness
// NPCS - dirty/half-dirty, PC - doesn't care, animals - Clean/half-dirty
// inital respect = 40 (smile/lickFace + 1 || grumble/growl - 1)
// NPCs, animals can perform actions when not in same room as PC
// PC can clean or dirty a room or make creature do it in current room
// creatures might have to leave to an adjacent room if room state changes and creature can't stay
// adjacent room must be fit, if not creature will change state it make it fit
// if all adjacent rooms are full creature will drill a hole and leave house (disappear from simulation)
// every creature in room with PC will grumble or growl once or smile and lickFace
// PC can make creature leave room in their choice of direction
// if creature can't be moved the creature will stay and growl or grumble
// NPCs - smile or grumble
// animals - lickFace or growl
// creatures that leave the room react before they leave
// if PC makes creature clean/dirty room, creatures reacts three times to state change (smile or lick 3 times)
// respect below 0 means PC loses, respect above 80 means PC wins

// upon start up user provides input file with info about world (rooms and creatures)
// parse file and create rooms/creatures and PC
// present user with list of commands - output then prompt for input
// commands: look, clean, dirty, <creature> clean, <creature dirty>, exit 

// room object (id, state, neighbors)
// creature object -> subclasses: PC, NPC, animal

int main(int argc, char** argv){
	int respect = 40;
	int num_rooms, num_creatures, state, north_id, south_id, east_id, west_id = 0;
	int creature_type, creature_location = 0;
	Room* rooms[3] = {};
	vector<Creature*> creatures;

	if (argc == 2){
		ifstream file(argv[1]);
		if (file.is_open()){
			file >> num_rooms;
			for (int i = 0; i < num_rooms; i++){
				if (file >> state >> north_id >> south_id >> east_id >> west_id){
					int neighbor_ids[4] = {north_id, south_id, east_id, west_id};
					Room* room = new Room(i, state, neighbor_ids);
					rooms[i] = room;
				}
			}
			file >> num_creatures;
			for (int i = 0; i < num_creatures; ++i){
				if (file >> creature_type >> creature_location){
					if (creature_type == 0){
						//PC
					} else if (creature_type == 1){
						//animal
						Animal* a = new Animal();
						a->set_id(i);
						creature_location = a->set_current_room(rooms, num_rooms, creature_location);
						rooms[creature_location]->add_creature(a);
						creatures.push_back(a);
						cout << "Animal id: " << a->get_id() << endl;
					} else if (creature_type == 2){
						//NPC
					} else {
						//error
					}
					Creature* c = new Creature();
					c->set_id(i);
					creature_location = c->set_current_room(rooms, num_rooms, creature_location);
					rooms[creature_location]->add_creature(c);
					creatures.push_back(c);
				}
			}
		}
	} else {
		cout << "Unexpected amount of arguments" << endl;
	}
	for (int i = 0; i < num_rooms; i++){
		rooms[i]->init_neighbors(rooms);
		rooms[i]->print_neighbors();
	}

	for (int i = 0; i < num_creatures; i++){
		creatures[i]->look();
		//cout << "Creature id: " << creatures[i]->get_id() << endl;
		//cout << "Room location: " << creatures[i]->get_room()->get_id() << endl;
	}


	return 0;
}
