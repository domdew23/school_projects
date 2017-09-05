#include <iostream>
#include <fstream>
#include "room.h"
using namespace std;

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
	}
	if (neighbor_ids[1] != -1){
		set_south(rooms[neighbor_ids[1]]);
	}
	if (neighbor_ids[2] != -1){
		set_east(rooms[neighbor_ids[2]]);
	}
	if (neighbor_ids[3] != -1){
		set_west(rooms[neighbor_ids[3]]);
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

void Room::print_neighbors(){
	cout << "Room #" << id << " has following neighbors:" << endl;
	if (neighbors[0] != NULL){
		cout << "Room #" << neighbors[0]->get_id() << " to the north with state " << neighbors[0]->get_state() << endl;
	}
	if (neighbors[1] != NULL){
		cout << "Room #" << neighbors[1]->get_id() << " to the south with state " << neighbors[1]->get_state() << endl;
	}
	if (neighbors[2] != NULL){
		cout << "Room #" << neighbors[2]->get_id() << " to the east with state " << neighbors[2]->get_state() << endl;
	}
	if (neighbors[3] != NULL){
		cout << "Room #" << neighbors[3]->get_id() << " to the west with state " << neighbors[3]->get_state() << endl;
	}
}
