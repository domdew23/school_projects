#ifndef ANIMAL_H
#define ANIMAL_H
#include "creature.h"

class Creature;
class Animal: public Creature{
	public:
		void react();
};

#endif