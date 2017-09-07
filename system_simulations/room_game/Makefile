output: main.o room.o creature.o animal.o pc.o npc.o
	g++ main.o room.o creature.o animal.o pc.o npc.o -o game

main.o: main.cpp
	g++ -c main.cpp

room.o: room.cpp room.h
	g++ -c room.cpp

creature.o: creature.cpp creature.h
	g++ -c creature.cpp

animal.o: animal.cpp animal.h
	g++ -c animal.cpp

pc.o: pc.cpp pc.h
	g++ -c pc.cpp

npc.o: npc.cpp npc.h
	g++ -c npc.cpp

clean:
	rm *.o game
