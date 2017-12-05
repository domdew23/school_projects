import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.ArrayList;

public class main {

	public static void main(String[] args){
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

		AtomicModel<Network, AtomicModel> press = new Machine<Network, AtomicModel>(new BigDecimal(1.0), "Press");
		AtomicModel<AtomicModel, Network> drill = new Machine<AtomicModel, Network>(new BigDecimal(2.0), "Drill");
		Scheduler<AtomicModel> scheduler = new Scheduler();

		Machine.setScheduler(scheduler);
		Machine.setTime(new Time(new BigDecimal(0.0), 0));

		Network<AtomicModel> network = Network.builder().addComponent(press).addComponent(drill).addInput(press).addOutput(drill).build();
		createCouples(press, drill, network);

		init(sc, network, scheduler);
		System.out.println(scheduler);

		while (!(scheduler.isEmpty())){
			System.out.println("Global time: " + Machine.getRealTime());
			Event<AtomicModel> eventToExecute = scheduler.pull();
			BigDecimal e = eventToExecute.time.getReal().subtract(Machine.getRealTime());
			
			Time interval = new Time(e, 1);
			Machine.setTime(Machine.getTime().advance(interval));

			for (AtomicModel model : network.getComponents()){
				execute(eventToExecute, model, e);
			}
			System.out.println("------------------------\n" + scheduler);
		}
	}

	private static void createCouples(AtomicModel one, AtomicModel two, Network<AtomicModel> network){
		one.addInput(network);
		one.addOutput(two);
		
		two.addInput(one);
		two.addOutput(network);
	}

	private static void execute(Event<AtomicModel> event, AtomicModel model, BigDecimal e){
		int output = 0;
		if (event.obj == model){
			if (model.getLast().getReal().compareTo(new BigDecimal(0.0)) == 0){
				event.updateE(new BigDecimal(0.0));
			} else {
				event.updateE(Machine.getRealTime().subtract(model.getLast().getReal()));
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


	private static void init(Scanner sc, Network<AtomicModel> network, Scheduler<AtomicModel> scheduler){
		BigDecimal lastTime = new BigDecimal(0.0);
		while (sc.hasNext()){
			BigDecimal time = new BigDecimal(sc.next());
			int q = sc.nextInt();
			BigDecimal e = time.subtract(lastTime);
			lastTime = time;

			for (AtomicModel model : network.getInputs()){
				Event<AtomicModel> event = Event.builder(new Time(time, 0), "deltaExternal", model).addParameter(e).addParameter(q).build();
				model.log(event);
				scheduler.put(event);
			}
		}
	}
}