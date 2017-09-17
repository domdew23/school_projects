#include <iostream>
#include <fstream>
#include <cstring>
#include <string>
#include <cstring>
#include <cstdlib>
#include <vector>
#include "room.h"
#include "creature.h"
#include "animal.h"
#include "pc.h"
#include "npc.h"
using namespace std;

void garbage_collection(Room* rooms[], vector<Creature*> creatures, int num_rooms){
	for (int i = 0; i < num_rooms; i++){
		for (int j = 0; j < rooms[i]->get_size(); j++){
			int id = 0;
			id = rooms[i]->get_creatures()[j]->get_id();
			delete creatures[id];
		}
		delete rooms[i];
	}
}

bool handle_leave(string input, Creature* creature, int* respect){
	if (input == "north"){
		creature->leave(0, input, respect);
		return true;
	} else if (input == "south"){
		creature->leave(1, input, respect);
		return true;
	} else if (input == "east"){
		creature->leave(2, input, respect);
		return true;
	} else if (input == "west"){
		creature->leave(3, input, respect);
		return true;
	} else {
		return false;
	}
}

int main(int argc, char** argv){
	int r = 40;
	int* respect = &r;
	int num_rooms, num_creatures, type, loc, state, north_id, south_id, east_id, west_id = 0;
	PC* pc; 
	Room* rooms[10] = {};
	vector<Creature*> creatures;

	if (argc == 2){
		ifstream file(argv[1]);
		if (file.is_open()){
			file >> num_rooms;
			if (num_rooms > 10){
				cout << "There can not be more than 10 rooms, please try again." << endl;
				exit(0);
			}
			for (int i = 0; i < num_rooms; i++){
				if (file >> state >> north_id >> south_id >> east_id >> west_id){
					int neighbor_ids[4] = {north_id, south_id, east_id, west_id};
					Room* room = new Room(i, state, neighbor_ids); // i is the room's id #
					rooms[i] = room;
				}
			}
			file >> num_creatures;
			if (num_creatures > 100){
				cout << "There can not be more than 100 creatures, please try again." << endl;
				exit(1);
			}
			for (int i = 0; i < num_creatures; i++){
				if (file >> type >> loc){
					if (type == 0){
						//PC
						pc = new PC(i, rooms, num_rooms, loc, "PC");
						creatures.push_back(pc);
					} else if (type == 1){
						//animal
						Animal* a = new Animal(i, rooms, num_rooms, loc, "animal");
						creatures.push_back(a);
					} else if (type == 2){
						//NPC
						NPC* npc = new NPC(i, rooms, num_rooms, loc, "NPC");
						creatures.push_back(npc);
					} else {
						cout << "Error parsing file.\nQuiting now..." << endl;
					}
				}
			}
		}
	} else {
		cout << "Unexpected amount of arguments" << endl;
	}

	for (int i = 0; i < num_rooms; i++){
		// initialize neighbhors for every room added
		rooms[i]->init_neighbors(rooms);
	}

	string input = "";
	int creat_id = 0;
	Creature* creature = NULL;
	bool is_creat = false;

	while(true) {
		if (*respect >= 80){
			cout << "You win! ";
			break;
		} else if (*respect <= 0){
			cout << "You lose. ";
			break;
		}

		cin >> input;
		if (isdigit(input[0])){
			creat_id = atoi(input.substr(0, input.find(":")).c_str());
			input = input.substr(input.find(":") + 1, input.length());
			creature = creatures[creat_id];
			is_creat = true; // checks if the user wants a creature to perform an action
		} else { 
			is_creat = false; // if not the pc is the one performing the action
			creature = pc;
		}

		for (int i = 0; i < input.length(); i++){
			input[i] = tolower(input[i]);
		}

		if (!pc->get_current_room()->contains(creature)){
			cout << "You can only apply commands to creatures in the your current room." << endl;
			continue;
		}

		if (input == "look"){
			creature->look();
		} else if (input == "clean"){
			creature->clean(creature, respect);
		} else if (input == "dirty"){
			creature->dirty(creature, respect);
		} else {
			if (!handle_leave(input, creature, respect)){
				if (input == "exit"){
					break;
				} else {
					cout << "Please enter a valid command." << endl;
				}
			}
		}
		cout << endl;
	}
	cout << "You finished with a respect of " << *respect << endl;

	garbage_collection(rooms, creatures, num_rooms);
	return 0;
}
