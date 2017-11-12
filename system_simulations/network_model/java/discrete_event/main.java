import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Random;

public class main {
	public static void main(String[] args){
		/* Network consists of two machines for working metal
		Machines are connected sequentially:
		- output from the press goes directly to the input of the drill
		- input to the network is coupled with input to the press
		- output from the drill is coupled with the output from the network 
		state:
		p - parts for the machine process
		s - time remaining to process the first of those parts.

		*/

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

		AtomicModel<Network, Machine> press = new Machine<Network, Machine>(new BigDecimal("1.0"), "Press");
		AtomicModel<Machine, Network> drill = new Machine<Machine, Network>(new BigDecimal("2.0"), "Drill");
		Scheduler scheduler = new Scheduler();

		Machine.setScheduler(scheduler);
		Machine.setTime(new Time(new BigDecimal("0.0"), 0));

		Network network = Network.builder().addComponent(press).addComponent(drill).addInput(press).addOutput(drill).build();
		network.addInputToOutput(press, drill);
		press.addInput(network);
		drill.addOutput(network);

		init(sc, network, scheduler);
		System.out.println(scheduler.toString());

		while (!(scheduler.isEmpty())){
			scheduler.checkMerge();
			Event eventToExecute = scheduler.pull();
			BigDecimal time = eventToExecute.time.getReal();
			BigDecimal e = time.subtract(Machine.getTime().getReal());

			/* advance time */
			Time interval = new Time(e, 1);
			Machine.setTime(Machine.getTime().advance(interval));
			System.out.println("Global time set to: " + Machine.getTime().getReal());
			
			for (AtomicModel model : network.getComponents()){
				execute(eventToExecute, model);
			}
			System.out.println("------------------------\n" + scheduler.toString());
		}
	}

			/*if (scheduler.peek().time < time){
				// execute external
				double e = time - Machine.getTime();
				for (AtomicModel m : network.getInputs()){
					m.deltaExternal(e, q);
				}
			} else {
				runEvent(scheduler.pull());
			}
		}

		Time interval = new Time(e, 1);
		Machine.setTime(Machine.getTime().advance(interval));
		if (scheduler.peek().time )
		}
	}*/

	private static void execute(Event event, AtomicModel model){
		if (event.model == model){
			switch (event.kind){
				case "deltaExternal":
					model.deltaExternal(event.e, event.q);
					break;
				case "deltaInternal":
					model.deltaInternal();
					break;
				case "deltaConfluent":
					model.deltaConfluent(event.q);
					break;
				case "lambda":
					model.lambda();
				default:
					System.out.println("Something went wrong");
					break;
			}
		}	
	}


	private static void init(Scanner sc, Network network, Scheduler scheduler){
		BigDecimal lastTime = new BigDecimal("0.0");
		
		Event[] events = new Event[10];
		int i = 0;
		
		while (sc.hasNext()){
			BigDecimal time = new BigDecimal(sc.next());
			int q = sc.nextInt();
			BigDecimal e = time.subtract(lastTime);
			lastTime = time;

			for (AtomicModel model : network.getInputs()){
				Event event = Event.builder(new Time(time, 0), "deltaExternal", model).addParameter(e).addParameter(q).build();
				events[i++] = event;
				scheduler.put(event);
			}
		}
	}
}