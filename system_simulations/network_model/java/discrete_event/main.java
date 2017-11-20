import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.ArrayList;

public class main {

	public static void main(String[] args){
		/* 
		Network consists of two machines for working metal
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

		AtomicModel<Network, Machine> press = new Machine<Network, Machine>(new BigDecimal(1.0), "Press");
		AtomicModel<Machine, Network> drill = new Machine<Machine, Network>(new BigDecimal(2.0), "Drill");
		Scheduler scheduler = new Scheduler();

		Machine.setScheduler(scheduler);
		Machine.setTime(new Time(new BigDecimal(0.0), 0));

		Network network = Network.builder().addComponent(press).addComponent(drill).addInput(press).addOutput(drill).build();
		network.addInputToOutput(press, drill);
		press.addInput(network);
		drill.addOutput(network);

		init(sc, network, scheduler);
		System.out.println(scheduler);

		while (!(scheduler.isEmpty())){
			System.out.println("Global time: " + Machine.getTime().getReal());
			scheduler.checkMerge();
			Event eventToExecute = scheduler.pull();
			BigDecimal e = eventToExecute.time.getReal().subtract(Machine.getTime().getReal());
			
			/* advance time */
			Time interval = new Time(e, 1);
			Machine.setTime(Machine.getTime().advance(interval));


			for (AtomicModel model : network.getComponents()){
				execute(eventToExecute, model, e);
			}
			System.out.println("------------------------\n" + scheduler);
		}
	}

	private static void execute(Event event, AtomicModel model, BigDecimal e){
		int output = 0;
		if (event.model == model){
			if (model.getLast().getReal().compareTo(new BigDecimal(0.0)) == 0){
				event.updateE(new BigDecimal(0.0));
			} else {
				event.updateE(Machine.getTime().getReal().subtract(model.getLast().getReal()));
			}
			model.log(event);
			switch (event.kind){
				case "lambda":
					output = model.lambda();
					break;
				case "deltaExternal":
					model.deltaExternal(event.e, event.q);
					break;
				case "deltaInternal":
					model.lambda();
					model.deltaInternal();
					break;
				case "deltaConfluent":
					model.lambda();
					model.deltaConfluent(event.q);
					break;
				default:
					System.out.println("Something went wrong");
					break;
			}
			System.out.println("Executing " + event);
		}	
	}


	private static void init(Scanner sc, Network network, Scheduler scheduler){
		BigDecimal lastTime = new BigDecimal(0.0);
		while (sc.hasNext()){
			BigDecimal time = new BigDecimal(sc.next());
			int q = sc.nextInt();
			BigDecimal e = time.subtract(lastTime);
			lastTime = time;

			for (AtomicModel model : network.getInputs()){
				Event event = Event.builder(new Time(time, 0), "deltaExternal", model).addParameter(e).addParameter(q).build();
				model.log(event);
				scheduler.put(event);
			}
		}
	}
}