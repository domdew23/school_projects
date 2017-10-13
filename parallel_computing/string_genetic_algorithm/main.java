import java.util.Random;
import java.util.Arrays;
import java.lang.Math;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.atomic.*;

class main{
	
	public static final String CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()-_+=[]{}<>,.?/:;'\"\\";
	public static final int POP_SIZE = 40;
	public static final int THREAD_COUNT = 4;
	public static final String GOAL = "And chubby on sum, EMMM UMMM EMM UMM";
	public static final int GOAL_SIZE = GOAL.length();
	public static final double MUTATION_RATE = 0.024;
	public static int generation = 0;

	public static AtomicReferenceArray<Person> new_population; 
	public static AtomicReference<Person> best_mem;
	
	public static void main(String[] args){
		Person init_mem = new Person();
		AtomicReference<Person> best_mem = new AtomicReference<Person>(init_mem);
		new_population = new AtomicReferenceArray<Person>(POP_SIZE);

		init(new_population, best_mem);
		run(new_population, best_mem);
	}

			/*Person[] old_pop = get_new_population(workers[0].new_population);
			//System.out.println(old_pop:  + old_pop[0].DNA);
			//for (int i = 0; i < THREAD_COUNT; i++){
				Person[] new_pop = new Person[POP_SIZE];
				for (int j = 0; j < POP_SIZE; j++){
					int rand = r.nextInt(POP_SIZE);
					int rand2 = r.nextInt(POP_SIZE);
					new_pop[j] = combine(old_pop[rand], old_pop[rand2]);
				}*/

	public static void run(AtomicReferenceArray<Person> new_population, AtomicReference<Person> best_mem){
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		Random r = new Random();
		for (int i = 0; i < THREAD_COUNT; i++){
			Person[] population = new Person[POP_SIZE];
			for (int j = 0; j < POP_SIZE; j++){
				int rand = r.nextInt(POP_SIZE);
				int rand2 = r.nextInt(POP_SIZE);
				population[j] = combine(new_population.get(rand), new_population.get(rand2));
			}
		//if (best_mem.get().fitness != 100){
			System.out.println(new_population.get(0).DNA);
			Worker w = new Worker(population, best_mem, new_population);
			executor.submit(w);
		}

		executor.shutdown();
		while(!executor.isTerminated()){/*something to do in main while other threads run*/}
		generation++;
		if (best_mem.get().fitness != 100){
			run(new_population, best_mem);
		}
	}

	public static void init(AtomicReferenceArray<Person> new_population, AtomicReference<Person> best_mem){
		Person[] population;

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++){
			population = init_population();
			Worker w = new Worker(population, best_mem, new_population);
			executor.submit(w);
		}

		executor.shutdown();
		while(!executor.isTerminated()){/*something to do in main while other threads run*/}
		generation++;
	}

	public static void go(){
		Person population[];
		Person best_member;

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

		boolean init = true;

		Thread[] threads = new Thread[THREAD_COUNT];
		Worker[] workers = new Worker[THREAD_COUNT];
		long start = System.currentTimeMillis();

		for (int i = 0; i < THREAD_COUNT; i++){
			population = init_population();
			//Worker w = new Worker(population, best_member);
			//workers[0] = w;
			//executor.submit(w);

			/*Thread t = new Thread(w);
			threads[i] = t;
			//t.start();*/
		}

		executor.shutdown();
		while(!executor.isTerminated()){}

		/*for (int i = 0; i < THREAD_COUNT; i++){
			threads[i].start();
		}
		for (int i = 0; i < THREAD_COUNT; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e){}	
		}
		System.out.println("Generation: " + generation);
		generation++;
		*/
		ExecutorService main_executor = Executors.newFixedThreadPool(THREAD_COUNT);

		Random r = new Random();
		best_member = workers[0].best_member.get();	

		while (best_member.fitness != 100){
			Person[] old_pop = get_new_population(workers[0].new_population);
			System.out.println("old_pop: " + old_pop[0].DNA);
			//for (int i = 0; i < THREAD_COUNT; i++){
				Person[] new_pop = new Person[POP_SIZE];
				for (int j = 0; j < POP_SIZE; j++){
					int rand = r.nextInt(POP_SIZE);
					int rand2 = r.nextInt(POP_SIZE);
					new_pop[j] = combine(old_pop[rand], old_pop[rand2]);
				}
				//Worker w = new Worker(new_pop, best_member);
				//main_executor.submit(w);
				//workers[0] = w;
				//Thread t = new Thread(w);
				//threads[i] = t;
				//workers[i] = w;
				//t.start();

			/*for (int i = 0; i < THREAD_COUNT; i++){
				threads[i].start();
	
			}
			for (int i = 0; i < THREAD_COUNT; i++){
				try {
				threads[i].join();
				} catch (InterruptedException e){}
			}*/
			
			best_member = workers[0].best_member.get();	
			System.out.println("Generation: " + generation + " || best fitness: " + best_member.fitness + " || DNA: " 
				+ best_member.DNA);
			generation++;
			main_executor.shutdown();
		}
		long end = System.currentTimeMillis();
		System.out.println("Time taken: {" + ((end - start)) + "} miliseconds");
	}

		/*for (int i = 0; i < POP_SIZE; i++){
			System.out.println(workers[0].new_population.get(i).DNA);
		}*/

	public static Person[] get_new_population(AtomicReferenceArray<Person> old_population){
		Person[] new_population = new Person[POP_SIZE];
		for (int i = 0; i < POP_SIZE; i++){
			new_population[i] = old_population.get(i);
		}
		System.out.println("new: " + new_population[0].DNA);
		return new_population;
	}

	public static Person combine(Person one, Person two){
		//System.out.println("one dna: " + one.DNA + " two dna: " + two.DNA);
		Person child;
		String DNA = "";
		int rand = 0;
		Random r = new Random();
		for (int i = 0; i < GOAL_SIZE; i++){
			if (i % 2 == 0){
				//System.out.println("one dna: " + one.DNA + " || i: " + i);
				DNA += one.DNA.charAt(i);
			} else {
				//System.out.println("two dna: " + one.DNA + " || i: " + i);
				DNA += two.DNA.charAt(i);
			}
		}
		child = new Person(DNA);
		child.set_fitness(0);
		child.DNA = mutate(child.DNA);
		//System.out.println("child dna: " + child.DNA);
		return child;
	}

	public static String mutate(String DNA){
		char[] tmp_dna = DNA.toCharArray();
		for (int i = 0; i < GOAL_SIZE; i++){
			Random r = new Random();
			if (Math.random() <= MUTATION_RATE){
				// mutate
				char c = CHARS.charAt(r.nextInt(CHARS.length()));
				tmp_dna[i] = c; 
			}
		}
		String mutated = new String(tmp_dna);
		return mutated;
	}

	public static String get_best(Person one, Person two){
		String best = "";
		if (one.fitness > two.fitness){
			best = one.DNA;
		} else {
			best = two.DNA;
		}
		return best;
	}

	public static Person[] init_population(){
		Person population[] = new Person[POP_SIZE];
		Random r = new Random();
		String DNA;
		int index = 0;
		for (int i = 0; i < POP_SIZE; i++){
			DNA = "";
			for (int j = 0; j < GOAL_SIZE; j++){
				index = r.nextInt(CHARS.length());
				DNA += CHARS.charAt(index);
			}

			Person p = new Person(DNA);
			population[i] = p;	
			//System.out.println(population[i].DNA);
		}
		return population;
	}
}
