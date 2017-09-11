#include <iostream>
#include <string>
#include <vector>
#include <cstdlib>
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

int Creature::set_current_room(Room* r){
	current_room = r;
}

void Creature::look(){
	this->current_room->print_description();
}

Room* Creature::get_current_room(){
	return this->current_room;
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

void Creature::leave(int i, string txt){
	Room* room = current_room->get_neighbors()[i]; 
	if (room != NULL){
		current_room->remove_creature(this);
		room->add_creature(this);
		if (type == "PC"){
			cout << "You leave to the " << txt << endl;
		} else {
			if (type == "NPC"){
				if (room->get_state() == 0){
					//current_room->change_state("clean", )
				}
			}
			cout << id << " leaves to the " << txt << endl;			
		}
	} else {
		cout << "There is no neighbors to the " << txt << " of this room." << endl;
	}
}

void Creature::check_status(){
	int i = 0;
	while(true){
		i = rand() % 4;
		if (current_room->get_neighbors()[i] != NULL){
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
			leave(i, txt);
			return;
		}
	}
}

Creature::~Creature(){
	cout << "deconstructing" << endl;
}