import java.util.Random;
import java.util.Arrays;
import java.lang.Math;

class main{
	
	public static final String CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()-_+=[]{}<>,.?/:;'\"\\";
	public static final int POP_SIZE = 10000;
	public static final String GOAL = "And chubby on sum, EMMM UMMM EMM UMM";
	public static final int GOAL_SIZE = GOAL.length();
	public static final double MUTATION_RATE = 0.05;

	public static void main(String[] args){
		Person population[] = new Person[POP_SIZE];
		init_population(population);
		run(population); 
	}

	public static void run(Person[] population){
		int gen = 0;
		while(true){
			evaluate_fitness(population);
			for (int i = 0; i < 4; i++){
				System.out.println("Fitness: " + population[i].fitness + " ||  " + population[i].DNA);
				if (population[i].fitness == 100){
					return;
				}
			}
			System.out.println("Generation #" + gen);	
			create_mating_pool(population);
			gen++;
		}	
	}

	public static void init_population(Person population[]){
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
			System.out.println(population[i].DNA);
		}
	}
	
	public static void evaluate_fitness(Person population[]){
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
			
			double f = ((double)count/(double)GOAL_SIZE);
			fitness = (int) Math.round(f * 100);
			population[i].set_fitness(fitness);
		}

		Arrays.sort(population, (a, b) -> b.fitness - a.fitness);			
	}

	public static boolean contains(int x, int arr[]){
		for (int n : arr){
			if (x == n){
				return true;
			}
		}
		return false;
	}

	public static void create_mating_pool(Person population[]){
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
	
	public static Person combine(Person one, Person two){
		//System.out.println("one dna: " + one.DNA + " two dna: " + two.DNA);
		Person child;
		String DNA = "";
		int rand = 0;
		for (int i = 0; i < GOAL_SIZE; i++){
			if (i % 2 == 0){
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
}
