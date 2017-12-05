import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

public class main {
	public static void main(String[] args){
		// main function -> y(n+1) = y(n) XOR (x1(n) XOR x2(n))
		// x1(n)/x2(n) are the two one-bit inputs to the network at tick n
		// y(n) is the one-bit output of the network at tick n

		// input to the network is combined with input to XOR1
		// output of XOR1 is combined with input to XOR2
		// output of XOR2 is combined with output of the network
		// output of XOR2 is also combined with input to a memory atomic model, M
		// output of M is combined with input to XOR2
		// output and state values of each XOR model is a single bit
		// input set of each XOR models contains two bits {b1, b2}
		// output function of each XOR model is lamda(s) = {s}
		// state transition function of each XOR model is delta(s, {b1, b2}) = b1 XOR b2
		// input and output of M are a single bit
		// state transition function of M is delta((q1, q2), {x}) = (q2, x)

		// Clients should be able to:
		// 1) control rate at which time in the simulation advances
		// 2) be notified when any component, atomic or network, produces output and when an atomic component changes state
		// 3) inject input into a running simulation to support interactive simulations
		// state of M is a pair of bits (q1, q2)

		// make sure lambda and delta are independent of each other
		// lambda and delta can have similar functions to one another Couple<I,O> -> network has array of couples/loop through couples to find match then send

		if (args.length < 1){
			System.out.println("Please supply an input file.");
			return;
		}
		
		File file;
		Scanner sc;

		try{
			file = new File(args[0]);
			sc = new Scanner(file);
		} catch (FileNotFoundException e){
			System.out.println("File not found.");
			return;
		}

		Scheduler<AtomicModel> scheduler = new Scheduler<AtomicModel>();

		AtomicModel<Network, AtomicModel> xor1 = new XORModel<Network, AtomicModel>();
		AtomicModel<AtomicModel, AtomicModel> xor2 = new XORModel<AtomicModel, AtomicModel>();
		AtomicModel<AtomicModel, AtomicModel> memoryModel = new MemoryModel<AtomicModel, AtomicModel>();
		
		Network<AtomicModel> network = Network.builder().addComponent(xor1).addComponent(xor2).addComponent(memoryModel).addInput(xor1).addOutput(xor2).build();

		Time currentTime = new Time(new BigDecimal(0.0), 0);
		
		createCouples(xor1, xor2, memoryModel, network);
		init(sc, scheduler, network, currentTime);
		System.out.println(scheduler);
		while (!(scheduler.isEmpty())){
			System.out.println("Global time: " + currentTime.getReal());
			Event<AtomicModel> event = scheduler.pull();
			BigDecimal e = event.time.getReal().subtract(currentTime.getReal());
			Time interval = new Time(e,1);
			currentTime = currentTime.advance(interval);
			XORModel.currentTime = currentTime;
			MemoryModel.currentTime = currentTime;

			for (AtomicModel model : network.getComponents()){
				execute(event, model, network);
			}
			System.out.println("------------------------\n" + scheduler);
		}
	}

	private static void execute(Event<AtomicModel> event, AtomicModel model, Network<AtomicModel> network){
		if (event.obj == model){
			switch (event.kind){
				case "deltaExternal":
					model.deltaExternal(event.x);
					break;
				case "deltaInternal":
					model.lambda();
					model.deltaInternal();
					break;
				case "deltaConfluent":
					model.lambda();
					model.deltaConfluent(event.x);
					break;
				default:
					System.out.println("Something went wrong");
					break;
			}
			System.out.println("Executing " + event);
		}
	}

	private static void createCouples(AtomicModel xor1, AtomicModel xor2, AtomicModel memModel, Network network){
		xor1.addInput(network);
		xor1.addOutput(xor2);

		xor2.addInput(memModel);
		xor2.addInput(xor1);
		xor2.addOutput(network);
		xor2.addOutput(memModel);

		memModel.addInput(xor2);
		memModel.addOutput(xor2);
	}

	private static void init(Scanner sc, Scheduler<AtomicModel> scheduler, Network<AtomicModel> network, Time currentTime){
		XORModel.scheduler = scheduler;
		XORModel.currentTime = currentTime;
		XORModel.network = network;
		MemoryModel.scheduler = scheduler;
		MemoryModel.currentTime = currentTime;

		while (sc.hasNext()){
			BigDecimal time = new BigDecimal(sc.next());
			int[] tmp = new int[2];
			tmp[0] = sc.nextInt();
			tmp[1] = sc.nextInt();
			boolean[] X = new boolean[2];
			for (int i = 0; i < 2; i++){
				switch (tmp[i]){
					case 0: X[i] = false; break;
					case 1: X[i] = true; break;
					default: break;
				}
			}
			for (AtomicModel model : network.getInputs()){
				Event<AtomicModel> eventOne = Event.builder(new Time(time, 0), "deltaExternal", model).addParameter(X[0]).build();
				Event<AtomicModel> eventTwo = Event.builder(new Time(time, 0), "deltaExternal", model).addParameter(X[1]).build();
				scheduler.put(eventOne);
				scheduler.put(eventTwo);
			}
		}
	}
}







