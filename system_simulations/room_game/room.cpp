#include <iostream>
#include <string>
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
		set_north(rooms[neighbor_ids[0]]);
	} else {
		neighbors[0] = NULL;
	}
	if (neighbor_ids[1] != -1){
		set_south(rooms[neighbor_ids[1]]);
	} else {
		neighbors[1] = NULL;
	}
	if (neighbor_ids[2] != -1){
		set_east(rooms[neighbor_ids[2]]);
	} else {
		neighbors[2] = NULL;
	}
	if (neighbor_ids[3] != -1){
		set_west(rooms[neighbor_ids[3]]);
	} else {
		neighbors[3] = NULL;
	}
}

void Room::add_creature(Creature* c){
	if (creatures.size() < 10){
		size++;
		this->creatures.push_back(c);
	} else {
		cout << "Creature " << c->get_id() << " denied entry, room full" << endl;
	}
}

void Room::set_north(Room* r){
	neighbors[0] = r;
}

void Room::set_south(Room* r){
	neighbors[1] = r;
}

void Room::set_east(Room* r){
	neighbors[2] = r;
}

void Room::set_west(Room* r){
	neighbors[3] = r;
}

Room Room::get_north(){
	return *neighbors[0];
}

Room Room::get_south(){
	return *neighbors[1];
}

Room Room::get_east(){
	return *neighbors[2];
}

Room Room::get_west(){
	return *neighbors[3];
}

int Room::get_id(){
	return this->id;
}

int Room::get_state(){
	return this->state;
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
	cout << endl;
}

bool Room::is_full(){
	if (size == 10){
		return true;
	} else {
		return false;
	}
}

bool Room::contains(Creature* c){
	for (int i = 0; i < creatures.size(); i++){
		if (creatures[i] == c){
			return true;
		}
	}
	return false;
}
