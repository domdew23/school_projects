import java.util.Random;
import java.util.Arrays;

class main{
	
	public static final String CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()-_+=[]{}<>,.?/:;'\"\\";
	public static final int POP_SIZE = 100;
	public static final String GOAL = "And chewby on sum, UMM EMMM";
	public static final int GOAL_SIZE = GOAL.length();

	public static void main(String[] args){
		Person population[] = new Person[POP_SIZE];
		init_population(population);
		evaluate_fitness(population);
		Arrays.sort(population);
		for (int i = 0; i < 10; i++){
			System.out.println(population[i].fitness);
		}			
	}

	public static void init_population(Person population[]){
		//Person population[] = new Person[POP_SIZE];	
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
		float fitness;
		int count;
		for (int i = 0; i < POP_SIZE; i++){
			count = 0;
			fitness = 0;
			for (int j = 0; j < GOAL_SIZE; j++){
				if (population[i].DNA.charAt(j) == GOAL.charAt(j)){
					count++;	
				}
			}
			
			fitness = (float) count/GOAL_SIZE;
			population[i].set_fitness(fitness);
			
		}
		Arrays.sort(population, (a,b) -> a.fitness.compareTo(b.fitness));			
	}

	public static void create_mating_pool(Person population[]){
	}
}
