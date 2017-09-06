output: room.o main.o creature.o
	g++ room.o main.o creature.o -o game

room.o: room.cpp room.h
	g++ -c room.cpp

creature.o: creature.cpp creature.h
	g++ -c creature.cpp

main.o: main.cpp
	g++ -c main.cpp

clean:
	rm *.o game
