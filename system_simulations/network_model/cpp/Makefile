output: main.o network.o xormodel.o memorymodel.o
	g++ main.o network.o xormodel.o memorymodel.o -o network

main.o: main.cpp
	g++ -std=c++11 -c main.cpp

network.o: Network.cpp Network.h
	g++ -std=c++11 -c Network.cpp

xormodel.o: XORModel.cpp XORModel.h
	g++ -std=c++11 -c XORModel.cpp

memorymodel.o: MemoryModel.cpp MemoryModel.h
	g++ -std=c++11 -c MemoryModel.cpp

clean:
	rm *.o network