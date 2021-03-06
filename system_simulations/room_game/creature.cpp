#include <iostream>
#include <string>
#include <vector>
#include <cstdlib>
#include <time.h>
#include "room.h"
#include "animal.h"
#include "npc.h"
#include "creature.h"
using namespace std;

Creature::Creature(int id, Room** rooms, int num_rooms, int location, string type){
	this->id = id;
	this->type = type;
	for (int i = 0; i < num_rooms; i++){
		if (location == rooms[i]->get_id()){
			this->current_room = rooms[i];
			rooms[i]->add_creature(this);
		}
	}
}

void Creature::set_current_room(Room* r){
	current_room = r;
}

void Creature::look(){
	current_room->print_description();
}

Room* Creature::get_current_room(){
	return current_room;
}

int Creature::get_id(){
	return id;
}

string Creature::get_type(){
	return type;
}

void Creature::clean(Creature* creature, int* respect){
	current_room->change_state("clean", creature, respect);
}

void Creature::dirty(Creature* creature, int* respect){
	current_room->change_state("dirty", creature, respect);
}

void Creature::leave(int i, string txt, int* respect){
	Room* room = current_room->get_neighbors()[i]; 
	if (room != NULL){
		if (room->is_full()){
			cout << "Room full." << endl;
			return;
		}

		current_room->remove_creature(this);
		room->add_creature(this);

		if (type == "PC"){
			cout << "You leave to the " << txt << endl;
		} else {
			cout << id << " leaves to the " << txt << endl;			
		}
	} else {
		cout << "There is no neighbors to the " << txt << " of this room." << endl;
	}
}

void Creature::forced_leave(int* respect){
	int i = 0;
	int iter = 0;
	srand(time(NULL));
	while(true){
		i = rand() % 4;
		// after 500 attempts it is determined that all neighbors must be full
		if (iter >= 500){
			cout << "All rooms full. " << type << " " << id << " has drilled a hole in the ceiling and left the simulation." << endl;
			current_room->remove_creature(this);
			delete this;
			return; 
		}
		if (current_room->get_neighbors()[i] != NULL){
			if (!current_room->get_neighbors()[i]->is_full()){
				string txt = "";
				switch(i){
					case 0:
						txt = "north";
						break;
					case 1:
						txt = "south";
						break;
					case 2:
						txt = "east";
						break;
					case 3:
						txt = "west";
						break;
					default:
						cout << "Something went wrong in leaving room" << endl;
				}
				leave(i, txt, respect);
				return;
			}
		}
		iter++;
	}
}

Creature::~Creature(){
}
