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

// every room has at most 10 creatures including PC - creatures will be denied with "Room Full"
// creatures move between rooms (enter or leave)
// when a creature leaves one room it has to enter antoher
// 3 types of creatures: Player(PC), non-player(NPC), animals
// each creature has 'look' command which shows name, state, description of room as well as creatures in room
// room states: dirty, half-dirty, clean
// creatures have preferences on room cleanliness
// NPCS - dirty/half-dirty, PC - doesn't care, animals - Clean/half-dirty
// inital respect = 40 (sm, ile/lickFace + 1 || grumble/growl - 1)
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

bool handle_leave(string input, Creature* creature, int* respect){
	if (input == "north"){
		creature->leave(0, input, respect, false);
		return true;
	} else if (input == "south"){
		creature->leave(1, input, respect, false);
		return true;
	} else if (input == "east"){
		creature->leave(2, input, respect, false);
		return true;
	} else if (input == "west"){
		creature->leave(3, input, respect, false);
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
						cout << "Error parsing file.\nQuiting..." << endl;
					}
				}
			}
		}
	} else {
		cout << "Unexpected amount of arguments" << endl;
	}

	for (int i = 0; i < num_rooms; i++){
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
			is_creat = true;
		} else {
			is_creat = false;
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
			creature->clean(creature, respect, false);
		} else if (input == "dirty"){
			creature->dirty(creature, respect, false);
		} else {
			if (!handle_leave(input, creature, respect)){
				if (input == "exit"){
					cout << "Quiting..." << endl;
					break;
				} else {
					cout << "Please enter a valid command." << endl;
				}
			}
		}
		cout << endl;
	}
	cout << "You finished with a respect of " << *respect << endl;
	return 0;
}
