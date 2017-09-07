#include <iostream>
#include <string>
#include "creature.h"
#include "room.h"
using namespace std;

Creature::Creature(int id, Room* rooms[], int num_rooms, int location, string type){
	this->id = id;
	this->type = type;
	for (int i = 0; i < num_rooms; i++){
		if (location == rooms[i]->get_id()){
			this->current_room = rooms[i];
			rooms[i]->add_creature(this);
		}
	}
}

int Creature::set_current_room(Room* rooms[], int num_rooms, int location){
	for (int i = 0; i < num_rooms; i++){
		if (location == rooms[i]->get_id()){
			this->current_room = rooms[i];
			return i;
		}
	}
	cout << "Error setting creature location" << endl;
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

