public class Runner {
	Scheduler scheduler;
	public Runner(Scheduler scheduler){
		this.scheduler = scheduler;
	}

	/*public void run(){
		while (!(scheduler.isEmpty())){
			scheduler.checkMerge();
			Event event = scheduler.pull();
			for (AtomicModel m : network.getComponents()){
				if (event.model == m){
					switch (event.kind){
						case "deltaExternal":
							m.deltaExternal(event.e, event.q);
							break;
						case "deltaInternal":
							int output = m.lambda();
							m.deltaInternal(q);
							break;
						case "deltaConfluent":
							int output = m.lambda();
							m.deltaConfluent(event.q, output);
							break;
						default:
							System.out.println("Something went wrong");
							break;
					}
				}
			}
			double e = scheduler.peek() - Machine.getTime();
			Time interval = new Time(e, 0);
			event.time = event.time.advance();
		}
	}*/
}