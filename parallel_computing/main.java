import java.util.Random;
class main{
	
	public static final String CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()-_+=[]{}<>,.?/:;'\"\\";
	public static final int POP_SIZE = 100;
	public static final int GOAL_SIZE = 28;
	public static final String GOAL = "And chewby on sum, UMM EMMM";
	

	public static void main(String[] args){
		init_population();			
	}

	public static void init_population(){
		char population[][] = new char [POP_SIZE][GOAL_SIZE];	
		Random r = new Random();
		char element[] = new char[GOAL_SIZE];
		int index = 0;
		for (int i = 0; i < POP_SIZE; i++){
			for (int j = 0; j < GOAL_SIZE; j++){
				index  = r.nextInt(CHARS.length());
				element[j] = CHARS.charAt(index);
			}
			population[i] = element;	
			System.out.println(population[i]);
		}
	}
	
	public static void evaluate_fitness(){
		int score;
		//for each string in pop, every correct char score = count/string_size	
	}
}
