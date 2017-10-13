output: main.o VendingMachine.o
	g++ main.o VendingMachine.o -o vending_machine

VendingMachine.o: VendingMachine.cpp VendingMachine.h
	g++ -c VendingMachine.cpp

main.o: main.cpp
	g++ -c main.cpp

clean:
	rm *.o vending_machine

