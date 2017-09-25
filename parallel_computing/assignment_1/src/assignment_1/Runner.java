package assignment_1;

public class Runner implements Runnable{
	
	private Population population;
	public Population  main_population;
	private final int POPULATION_SIZE = 32;
	private double best_fitness;
	
	public Runner(Population population, Population main_population){
		this.population = population;
		this.main_population = main_population;
	}
	
	public void run(){
		for (Member m : population.members){
			m.reset();
		}
		get_neighbors();
		evaluation();
		population.sort();
		population.set_avg_fitness();
		
		for (int i = 0; i < population.members.length; i++){
			population.check_swap(population.members[i]);
		}
		update();

	}
	
	synchronized public void set_best(){
		if (population.avg_fitness > best_fitness){
			best_fitness = population.avg_fitness;
		}
	}
	
	public void evaluation(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			population.members[i].evaluate_fitness();
			//System.out.println("Member["+i+"] fitness: " + population.members[i].fitness);
		}
	}
	
	public void set_neighbor(int i, int shift, Member m){
		Member member;
		try {
			member = population.members[i+shift];
		} catch (Exception e){
			member = null;
		}
		m.add_neighbor(member);
	}
	
	public void get_neighbors(){
		for (int i = 0; i < POPULATION_SIZE; i++){
			set_neighbor(i, - 1, population.members[i]); // left
			set_neighbor(i, - 4, population.members[i]); // top
			set_neighbor(i, + 1, population.members[i]); // right
			set_neighbor(i, + 1, population.members[i]); // bottom
		}
	}
	
	synchronized public void update(){
		//System.out.println("id: " + population.members[0].best_fitness);
		//System.out.println(Thread.currentThread().getName() + " || fitness: " + this.population.avg_fitness + " || main fitness: " + main_population.avg_fitness);

		/*for (int i = 0; i < POPULATION_SIZE; i++){
			int tmp = 0;
			while (population.members[i].img != main_population.members[tmp].img){
				tmp++;
			}
			
			if (population.members[i].fitness > main_population.members[tmp].best_fitness){
				for (Member m : main_population.members){
					if (m.img != population.members[i].img){
						if (m.bestX == population.members[i].x && m.bestY == population.members[i].y){
							return;
						}
					}
				}
				main_population.members[tmp].bestX = population.members[i].x;
				main_population.members[tmp].bestY = population.members[i].y;
				main_population.members[tmp].best_fitness = population.members[i].fitness;
			}
		}*/
		
		if (this.population.avg_fitness > main_population.avg_fitness){
			main_population = this.population;
		}
	}
}
