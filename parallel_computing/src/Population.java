public class Population {
	double fitness;
	int WIDTH = 8;
	int HEIGHT = 4;
	Member[] members;
	int id;

	Population(int pop_size){
		this.members = new Member[pop_size];
		this.fitness = 0.0;
	}
}
