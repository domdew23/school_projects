output: room.o main.o creature.o animal.o
	g++ room.o main.o creature.o animal.o -o game

room.o: room.cpp room.h
	g++ -c room.cpp

creature.o: creature.cpp creature.h
	g++ -c creature.cpp

animal.o: animal.cpp animal.h
	g++ -c animal.cpp

main.o: main.cpp
	g++ -c main.cpp

clean:
	rm *.o game
