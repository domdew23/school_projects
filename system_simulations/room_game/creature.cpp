#include <iostream>
#include "creature.h"
#include "room.h"
using namespace std;

Creature::Creature(int id){
	this->id = id;
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
	return this->id;
}

