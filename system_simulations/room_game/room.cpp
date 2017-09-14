#include <iostream>
#include <string>
#include <vector>
#include "creature.h"
#include "room.h"
using namespace std;

Room::Room(int id, int state, int ids[]){
	this->id = id;
	this->state = state;
	size = 0;
	for (int i = 0; i < 4; i++){
		neighbor_ids[i] = ids[i];
	}
}

void Room::init_neighbors(Room* rooms[]){
	if (neighbor_ids[0] != -1){
		neighbors[0] = rooms[neighbor_ids[0]];
	} else {
		neighbors[0] = NULL;
	}
	if (neighbor_ids[1] != -1){
		neighbors[1] = rooms[neighbor_ids[1]];
	} else {
		neighbors[1] = NULL;
	}
	if (neighbor_ids[2] != -1){
		neighbors[2] = rooms[neighbor_ids[2]];
	} else {
		neighbors[2] = NULL;
	}
	if (neighbor_ids[3] != -1){
		neighbors[3] = rooms[neighbor_ids[3]];
	} else {
		neighbors[3] = NULL;
	}
}

bool Room::add_creature(Creature* c){
	if (!is_full()){
		size++;
		this->creatures.push_back(c);
		c->set_current_room(this);
		if (state == 2 && c->get_type() == "animal"){
			state = 1;
		} else if (state == 0 && c->get_type() == "NPC"){
			state = 1;
		}
		return true;
	} else {
		return false;	
	}
}

bool Room::remove_creature(Creature* c){
	for (int i = 0; i < creatures.size(); i++){
	 	if (creatures[i] == c){
	 		creatures.erase(creatures.begin() + i);
	 		size--;
	 		return true;
	 	}
	 }
	 return false; 
}


int Room::get_id(){
	return this->id;
}

int Room::get_state(){
	return this->state;
}

vector<Creature*> Room::get_creatures(){
	return creatures;
}

void Room::print_description(){
	string txt;
	switch(this->state){
		case 0:
			txt = "Clean";
			break;
		case 1:
			txt = "Half-dirty";
			break;
		case 2:
			txt = "Dirty";
			break;
	}
	cout << "Room #" << id << " is " << txt << " and has following neighbors: ";
	for (int i = 0; i < 4; i++){
		if (neighbors[i] != NULL){
			string direction = "";
			switch (i){
				case 0: 
					direction = "North";
					break;
				case 1:
					direction = "South";
					break;
				case 2:
					direction = "East";
					break;
				case 3:
					direction = "West";
					break;
			}
			cout << "room #" << neighbors[i]->get_id() << " to the " << direction << ", ";
		}
	}
	cout << "and contains: " << endl;
	for(int i = 0; i < this->creatures.size(); i++){
		cout << creatures[i]->get_type() << ", id: " << creatures[i]->get_id() << endl;
	}
}

bool Room::is_full(){
	if (size >= 10){
		return true;
	}
	return false;
}

bool Room::contains(Creature* c){
	for (int i = 0; i < creatures.size(); i++){
		if (creatures[i] == c){
			return true;
		}
	}
	return false;
}

Room** Room::get_neighbors(){
	return neighbors;
}

void Room::change_state(string change, Creature* creature, int* respect, bool forced){
	Creature* tmp[size];
	for (int i = 0; i < size; i++){
		tmp[i] = creatures[i];
	}
	
	bool cleaning = false;
	switch (state){
		case 0:
			if (change == "clean"){
				cleaning = true;
				cout << "This room is already clean." << endl;
				return;
			} else {
				//cout << "Dirtying..." << endl;
				state = 1;
				break;
			}
		case 1:
			if (change == "clean"){
				cleaning = true;
				//cout << "Cleaning..." << endl;
				state = 0;
				break;
			} else {
				//cout << "Dirtying..." << endl;
				state = 2;
				break;
			}
		case 2:
			if (change == "clean"){
				cleaning = true;
				//cout << "Cleaning..." << endl;
				state = 1;
				break;				
			} else  {
				cout << "This room is already dirty." << endl;
				return;	
			}
		default:
			cout << "Something wrong with room state" << endl;
	}

	bool this_creat = false;
	bool needs_to_leave = false;
	for (int i = 0; i < size; i++){
		if (tmp[i]->get_type() != "PC"){
			if (creature == tmp[i]){
				this_creat = true;
			} else {
				this_creat = false;
			}
			if (cleaning){
				needs_to_leave = tmp[i]->react("clean", this_creat, respect);
			} else {
				needs_to_leave = tmp[i]->react("dirty", this_creat, respect);
			}
			if (needs_to_leave){
				tmp[i]->forced_leave(respect);
			}
		}
	}
}

int Room::get_size(){
	return size;
}

Room::~Room(){
}

