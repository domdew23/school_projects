output: main.o Scheduler.o Event.o Time.o AtomicModel.o Machine.o Network.o
	g++ -std=c++11 main.o Scheduler.o Event.o Time.o AtomicModel.o Machine.o Network.o -o network

Network.o: Network.cpp
	g++ -std=c++11 -c Network.cpp

Machine.o: Machine.cpp
	g++ -std=c++11 -c Machine.cpp

AtomicModel.o: AtomicModel.cpp
	g++ -std=c++11 -c AtomicModel.cpp

Time.o: Time.cpp
	g++ -std=c++11 -c Time.cpp

Event.o: Event.cpp
	g++ -std=c++11 -c Event.cpp

Scheduler.o: Scheduler.cpp
	g++ -std=c++11 -c Scheduler.cpp

main.o: main.cpp
	g++ -std=c++11 -c main.cpp

clean:
	rm *.o network