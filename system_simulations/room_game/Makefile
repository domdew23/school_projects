output: room.o main.o
	g++ room.o main.o -o game

room.o: room.cpp room.h
	g++ -c room.cpp

main.o: main.cpp
	g++ -c main.cpp

clean:
	rm *.o game