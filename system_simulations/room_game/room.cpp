#include <iostream>
#include <fstream>
#include "room.h"
using namespace std;

Room::Room(){
	id = -1;
	state = -9999;
}
Room::Room(int id, int state, int ids[]){
	this->id = id;
	this->state = state;
	for (int i = 0; i < 4; i++){
		neighbor_ids[i] = ids[i];
	}
}

void Room::init_neighbors(Room* rooms[]){
	if (neighbor_ids[0] != -1){
		set_north(rooms[neighbor_ids[0]]);
		cout << "North ID: " << neighbors[0]->get_id() << " North State: " << neighbors[0]->get_state() << endl;
	}
	if (neighbor_ids[1] != -1){
		set_south(rooms[neighbor_ids[1]]);
		cout << "South ID: " << neighbors[1]->get_id() << " South State: " << neighbors[1]->get_state() << endl;
	}
	if (neighbor_ids[2] != -1){
		set_east(rooms[neighbor_ids[2]]);
		cout << "East ID: " << neighbors[2]->get_id() << " East State: " << neighbors[2]->get_state() << endl;
	}
	if (neighbor_ids[3] != -1){
		set_west(rooms[neighbor_ids[3]]);
		cout << "West ID: " << neighbors[3]->get_id() << " West State: " << neighbors[3]->get_state() << endl;
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
