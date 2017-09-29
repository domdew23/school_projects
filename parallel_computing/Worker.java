import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.*;

public class Worker implements Runnable {
	private static final int POP_SIZE = 40;
	private static final int THREAD_COUNT = 4;
	private static final String GOAL = "And chubby on sum, EMMM UMMM EMM UMM";
	private static final int GOAL_SIZE = GOAL.length();

	private Person[] population = new Person[POP_SIZE];
	public static AtomicReferenceArray<Person> new_population; 
	public static AtomicReference<Person> best_member;
	private static AtomicInteger index = new AtomicInteger(0);
	private Lock lock = new ReentrantLock();

	public Worker(Person[] population, AtomicReference<Person> best_member, AtomicReferenceArray<Person> new_population){
		this.population = population;
		this.best_member = best_member;
		this.new_population = new_population;
	}

	public void run(){
		evaluate_fitness();
		set_best();
		//System.out.println(Thread.currentThread().getName() + " best member: " + best_member.get().DNA + "|| fitness: "
		//	+ best_member.get().fitness);
		//print();
		create_mating_pool();
		System.out.println(Thread.currentThread().getName() + " fitness: " + population[0].DNA);

	}

	public void print(){
		for (int i = 0; i < 5; i++){
			System.out.println(Thread.currentThread().getName() + " DNA: " + population[i].DNA + " || fitness: "
			+ population[i].fitness);
		}
	}

	public void evaluate_fitness(){
		int fitness;
		int count;
		for (int i = 0; i < POP_SIZE; i++){
			count = 0;
			fitness = 0;
			for (int j = 0; j < GOAL_SIZE; j++){
				if (population[i].DNA.charAt(j) == GOAL.charAt(j)){
					count++;	
				}
			}
			//System.out.println("thread: " + id + " || count: " + count);
			double f = ((double)count/(double)GOAL_SIZE);
			fitness = (int) Math.round(f * 100);
			//System.out.println("thread: " + id + " || fitness: " + fitness);
			population[i].set_fitness(fitness);
		}
		Arrays.sort(population, (a, b) -> b.fitness - a.fitness);
		//System.out.println("thread: " + id + " || population["+0+"] fitness: " + population[0].fitness);
	}

	public void set_best(){
		if (population[0].fitness > best_member.get().fitness){
			best_member.set(population[0]);
		}
	}

	public void create_mating_pool(){
		lock.lock();
		try {
			for (int i = 0; i < population.length/THREAD_COUNT; i++){
				//System.out.println(Thread.currentThread().getName() + " adding " + population[i].fitness + " to new population");
				new_population.set(index.getAndIncrement(), population[i]);
			}
		} finally{
			lock.unlock();
		}
	}
}


// start thread with population
// evaluate fitness for each member of each thread (wait)
// take members with highest fitness from each thread back in main thread
// create new population with members with highest fitness from each thread
// notify threads to continue with new population
