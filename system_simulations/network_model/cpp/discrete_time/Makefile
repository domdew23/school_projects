output: main.o Network.o XORModel.o MemoryModel.o
	g++ main.o Network.o XORModel.o MemoryModel.o -o network

main.o: main.cpp
	g++ -std=c++11 -c main.cpp

Network.o: Network.cpp Network.h
	g++ -std=c++11 -c Network.cpp

XORModel.o: XORModel.cpp XORModel.h
	g++ -std=c++11 -c XORModel.cpp

MemoryModel.o: MemoryModel.cpp MemoryModel.h
	g++ -std=c++11 -c MemoryModel.cpp

clean:
	rm *.o network
