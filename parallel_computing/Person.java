
class Person {

	String DNA;
	int fitness;
	
	Person(String DNA){
		this.DNA = DNA;
	}

	Person(){
		this.DNA = "";
		this.fitness = 0;
	}

	public void set_fitness(int fitness){
		this.fitness = fitness;
	}

}
