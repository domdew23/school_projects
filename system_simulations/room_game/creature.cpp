#include <iostream>
#include <string>
#include <vector>
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

void Creature::clean(){
	current_room->clean();
}

void Creature::dirty(){
	current_room->dirty();
}

void Creature::leave(int i, string txt){
	Room* room = current_room->get_neighbors()[i]; 
	if (room != NULL){
		current_room->remove_creature(this);
		room->add_creature(this);
	} else {
		cout << "There is no neighbors to the " << txt << " of this room." << endl;
	}
}

Creature::~Creature(){
	cout << "deconstructing" << endl;
}