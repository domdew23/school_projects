import java.util.Random;
import java.util.Arrays;


public class Job {
	public final String CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()-_+=[]{}<>,.?/:;'\"\\";
	public final int POP_SIZE = 10000;
	public final String GOAL = "And chubby on sum, EMMM UMMM EMM UMM";
	public final int GOAL_SIZE = GOAL.length();
	public final double MUTATION_RATE = 0.05;
	public Person[] population;
	private int total_generations = 0;
	int best_fitness = 0;
	int id = 0;
	boolean running = true;
	
	public Job(){
		
	}

	public void run(){
		while (running){
			increment();
			evaluate_fitness(population);
			set_best();
			print_best();
			check_best(); 
			//create_mating_pool(population);
		}
	}

	public void set_population(Person[] population){
		this.population = population;
	}

	synchronized private void check_best(){
		if (best_fitness == 100){
			running = false;
		}
	}
	synchronized private void print_best(){
		System.out.println("Generation #" + total_generations + " || Thread #" + this.id + " || Best match: " + population[0].DNA 
			+ " || Fitness: " + best_fitness + "\n");
	}

	synchronized private void set_best(){
		this.best_fitness = population[0].fitness;
	}

	synchronized private void increment(){
		total_generations++;
	}

	public void print_top4(){
		for (int i = 0; i < 4; i++){
			System.out.println("Thread #" + id + " || Fitness: " + population[i].fitness + " ||  " + population[i].DNA);
		if (population[i].fitness == 100){
			System.out.println("Fitness: " + population[i].fitness + " ||  " + population[i].DNA);
			return;
			}
		}
		System.out.println("\n---------------------");
	}

	public void evaluate_fitness(Person population[]){
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

	public boolean contains(int x, int arr[]){
		for (int n : arr){
			if (x == n){
				return true;
			}
		}
		return false;
	}

	public void create_mating_pool(Person population[]){
		Person mating_pool[] = new Person[POP_SIZE];
		Random r = new Random();
		int top_scores[] = {-1, -1, -1};
		int count = 0;

		for (int i = 0; i < POP_SIZE; i++){
			if (population[i].fitness == 0 || count == 3){
				break;
			}

			if (!contains(population[i].fitness, top_scores)){
				top_scores[count] = population[i].fitness;
				count++;
				continue;
			}
		}

		count = 0;
		for (int i = 0; i < POP_SIZE; i++){
				if (contains(population[i].fitness, top_scores)){
					mating_pool[count] = population[i];
					count++;
					continue;
				}
		}

		for (int i = 0; i < POP_SIZE; i++){
			int rand = r.nextInt(count);
			int rand2 = r.nextInt(count);
			population[i] = combine(mating_pool[rand], mating_pool[rand2]);
		}
	}
	
	public Person combine(Person one, Person two){
		//System.out.println("one dna: " + one.DNA + " two dna: " + two.DNA);
		Person child;
		String DNA = "";
		int rand = 0;
		Random r = new Random();
		for (int i = 0; i < GOAL_SIZE; i++){
			if (i % r.nextInt(2) == 0){
				DNA += one.DNA.charAt(i);
			} else {
				DNA += two.DNA.charAt(i);
			}
		}
		child = new Person(DNA);
		child.set_fitness(0);
		child.DNA = mutate(child.DNA);
		//System.out.println("child dna: " + child.DNA);
		return child;
	}

	public String mutate(String DNA){
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
}