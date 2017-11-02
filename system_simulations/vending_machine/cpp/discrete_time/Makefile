output: main.o VendingMachine.o
	g++ -std=c++11 main.o VendingMachine.o -o vending_machine

VendingMachine.o: VendingMachine.cpp VendingMachine.h
	g++ -std=c++11 -c VendingMachine.cpp

main.o: main.cpp
	g++ -std=c++11 -c main.cpp

clean:
	rm *.o vending_machine

