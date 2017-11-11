import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

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

		AtomicModel<Network, Machine> press = new Machine<Network, Machine>(1, "Press");
		AtomicModel<Machine, Network> drill = new Machine<Machine, Network>(2, "Drill");
		Scheduler scheduler = new Scheduler();

		Machine.setScheduler(scheduler);
		Machine.setTime(new Time(0, 0));

		Network<AtomicModel, AtomicModel> network = Network.builder().addComponent(press).addComponent(drill).addInput(press).addOutput(drill).build();
		network.addInputToOutput(press, drill);
		press.addInput(network);
		drill.addOutput(network);

		init(sc, network, scheduler);
		System.out.println(scheduler.toString());

		while (!(scheduler.isEmpty())){
			Event eventToExecute = scheduler.pull();
			double time = eventToExecute.time.getReal();
			double e = time - Machine.getTime().getReal();

			Time interval = new Time(e, 1);
			Machine.setTime(Machine.getTime().advance(interval));
			for (AtomicModel model : network.getComponents()){
				execute(eventToExecute, model);
			} 
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
					model.deltaInternal(model.lambda());
					break;
				case "deltaConfluent":
					model.deltaConfluent(event.q, model.lambda());
					break;
				default:
					System.out.println("Something went wrong");
					break;
			}
		}	
	}


	private static void init(Scanner sc, Network network, Scheduler scheduler){
		double lastTime = 0.0;
		while (sc.hasNext()){
			double time = sc.nextDouble();
			int q = sc.nextInt();
			double e = time - lastTime;
			lastTime = time;
			for (AtomicModel model : network.getInputs()){
				Event event = Event.builder(new Time(time, 0), "deltaExternal", model).addParameter(e).addParameter(q).build();
				scheduler.put(event);
			}
		}
	}
}