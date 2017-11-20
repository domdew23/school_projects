output: main.o VendingMachine.o Scheduler.o Time.o
	g++ -std=c++11 main.o VendingMachine.o Scheduler.o Time.o -o vending_machine

VendingMachine.o: VendingMachine.cpp VendingMachine.h
	g++ -std=c++11 -c VendingMachine.cpp

Scheduler.o: Scheduler.cpp Scheduler.h
	g++ -std=c++11 -c Scheduler.cpp

Time.o: Time.cpp Time.h
	g++ -std=c++11 -c Time.cpp

main.o: main.cpp
	g++ -std=c++11 -c main.cpp

clean:
	rm *.o vending_machine
