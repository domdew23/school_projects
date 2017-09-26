
import java.util.Arrays;
import java.util.Random;

public class Population {
	int size = 0;
	int pop_size;
	double avg_fitness = 0.0;
	double MUTATION_RATE = 0.01;
	int width = 8;
	int height = 4;
	Member[][] members;

	Population(int pop_size){
		this.pop_size = pop_size;
		members = new Member[width][height];
	}
	
	public void sort(){
		//Arrays.sort(members, (a, b) -> (int) a.fitness - (int) b.fitness);
	}
	
	public void print_fitnesses(){
		for (int i = 0; i < pop_size; i++){
			for (int j = 0; j < 4; j++){
				//System.out.println("members["+i+"]" + " neighbors["+i+"] fitness: " + members[i].fitness);
			}
		}
	}

	public void check_swap(Member mem){
		if (mem.bestX != -1 && mem.bestY != -1){
			return;
		}
		
		int neighbor_fitness = 0;
		int count = 0;
		for (int i = 0; i < 4; i++){
			neighbor_fitness = mem.new_neighbor_ids[i];
			System.out.println("member: " + mem.id + "neighbor fit: " + neighbor_fitness + " || expected: " + mem.neighbor_ids[i]);
			
			if (mem.neighbor_ids[i] == neighbor_fitness){
				count++;
			}
		}
		
		System.out.println("count: " + count);
		
		if (count == 4){
			mem.bestX = mem.x;
			mem.bestY = mem.y;
			return;
		}
		
		Random r = new Random();
		double odds = (double) count/5 * (r.nextDouble() + 1);
		
		System.out.println("count: " + count + " -- odds: " + odds);
		if (!(Math.random() <= odds)){
			swap(mem, mem.neighbors[r.nextInt(4)]);
		}
		
		if (Math.random() <= 0){
			int rand1 = r.nextInt(4);
			int rand2 = r.nextInt(4);
			try {
				swap(mem.neighbors[rand1], mem.neighbors[rand1].neighbors[rand2]);
			} catch (Exception e){
				return;
			}
		} 
		return;
	}
	
	public void swap(Member one, Member two){
			if (one.x == -1 || two.x == -1 || one == two){
				return;
			}

			int one_tmpX =  one.x;
			int one_tmpY = one.y;
			
			Member tmp_one = members[one.x][one.y];
	
			//System.out.println("Swapping: " + one.x + ", " + one.y + " with " + two.x + ", " + two.y);
			
			members[one.x][one.y] = members[two.x][two.y];
			members[two.x][two.y] = tmp_one;
			

			one.x = two.x;
			two.x = one_tmpX;
			
			one.y = two.y;
			two.y = one_tmpY;
			
			//System.out.println("After Swap: " + one.x + ", " + one.y + " with " + two.x + ", " + two.y);
			
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e){}
			*/
		}
	
	public int[] mutate(int[] pixels){
		for (int i = 0; i < pixels.length; i++){
			Random r = new Random();
			if (Math.random() <= MUTATION_RATE){
				int rand = r.nextInt(pixels.length);
				int temp = pixels[i];
				pixels[i] = pixels[rand];
				pixels[rand] = temp;
			}
		}
		return pixels;
	}
	
	public boolean contains(double value, double[] arr){
		for (double n : arr){
			if (value == n){
				return true;
			}
		}
		return false;
	}
	
	/*public void set_avg_fitness(){
		int total = 0;
		for (Member m : members){
			total += m.fitness;
		}
		this.avg_fitness = (double) total / (double) members.length;
		//System.out.println("Population avg fitness: " + avg_fitness);
	}*/
	
}
