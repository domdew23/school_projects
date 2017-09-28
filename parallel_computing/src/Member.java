
import processing.core.PImage;

public class Member{
	int x,y; 
	int bestX, bestY;
	double best_fitness = 0.0;

	//double[] red_hist = new double[4];
	//double[] green_hist = new double[4];
	//double[] blue_hist = new double[4];
	//double[] weights = {.7, .4, .1, .01};
	
	Member[] neighbors; // [0] left, [1] top, [2] right, [3] bottom
	double fitness;
	int[] neighbor_ids;
	int[] new_neighbor_ids;
	int[] affinites;
	
	PImage img;
	int id;
	
	Member(int id, int x, int y, PImage img){
		this.id = id;
		this.x = x;
		this.y = y;
		//this.bestX = -1;
		//this.bestY = -1;
		//this.fitness = 0;
		this.img = img;
		//this.neighbor_ids = new int[4];
		//this.new_neighbor_ids = new int[4];
		//neighbors = new Member[4];
		this.affinites = new int[32];
	}
	
	Member(){
		this.id = -9999;
		this.fitness = -9999;
		this.x = -1;
		this.y = -1;
	}
	
	/*
	public void evaluate_fitness(){
		double total = 0.0;
		for (int i = 0; i < neighbors.length; i++){
			switch(i){
				case 0:
					//left
					if (neighbors[i] == null){
						neighbor_ids[i]  = -1.0;
						break;
					}
					
					double[] left_red_diff = new double[4];
					double[] left_green_diff = new double[4];
					double[] left_blue_diff = new double[4];
					
					double left_red_sum = 0.0, left_green_sum = 0.0, left_blue_sum = 0.0;

					for (int j = 0; j < 4; j++){
						left_red_diff[j] = Math.abs(red_hist[j] - neighbors[i].red_hist[j]);
						left_red_sum += (left_red_diff[j] * weights[j]);
						
						left_green_diff[j] = Math.abs(green_hist[j] - neighbors[i].green_hist[j]);
						left_green_sum += (left_green_diff[j] * weights[j]);
						
						left_blue_diff[j] = Math.abs(blue_hist[j] - neighbors[i].blue_hist[j]);
						left_blue_sum += (left_blue_diff[j] * weights[j]);
					}
					
					neighbor_fitnesses[0] = left_red_sum + left_green_sum + left_blue_sum;
					total += neighbor_fitnesses[0] ;
					break;
				
				case 1:
					//top
					if (neighbors[i] == null){
						neighbor_fitnesses[i]  = -1.0;
						continue;
					}
					
					double[] top_red_diff = new double[4];
					double[] top_green_diff = new double[4];
					double[] top_blue_diff = new double[4];
					
					double top_red_sum = 0.0, top_green_sum = 0.0, top_blue_sum = 0.0;	

					for (int j = 0; j < 4; j++){
						top_red_diff[j] = Math.abs(red_hist[j] - neighbors[i].red_hist[j]);
						top_red_sum += (top_red_diff[j]* weights[j]);
						
						top_green_diff[j] = Math.abs(green_hist[j] - neighbors[i].green_hist[j]);
						top_green_sum += (top_green_diff[j]* weights[j]);
						
						top_blue_diff[j] = Math.abs(blue_hist[j] - neighbors[i].blue_hist[j]);
						top_blue_sum += (top_blue_diff[j]* weights[j]);
					}
					
					neighbor_fitnesses[1] = top_red_sum + top_green_sum + top_blue_sum;
					total += neighbor_fitnesses[1];
					break;
					
				case 2:
					//right
					if (neighbors[i] == null){
						neighbor_fitnesses[i] = -1.0;
						continue;
					}
					double[] right_red_diff = new double[4];
					double[] right_green_diff = new double[4];
					double[] right_blue_diff = new double[4];
					
					double right_red_sum = 0.0, right_green_sum = 0.0, right_blue_sum = 0.0;

					for (int j = 0; j < 4; j++){
						right_red_diff[j] = Math.abs(red_hist[j] - neighbors[i].red_hist[j]);
						right_red_sum += (right_red_diff[j]* weights[j]);
						
						right_green_diff[j] = Math.abs(green_hist[j] - neighbors[i].green_hist[j]);
						right_green_sum += (right_green_diff[j]* weights[j]);
						 
						right_blue_diff[j] = Math.abs(blue_hist[j] - neighbors[i].blue_hist[j]);
						right_blue_sum += (right_blue_diff[j]* weights[j]);
					} 

					neighbor_fitnesses[i] = right_red_sum + right_green_sum + right_blue_sum;
					total += neighbor_fitnesses[i];
					break;
				
				case 3:
					//bottom
					if (neighbors[i] == null){
						neighbor_fitnesses[i]= -1.0;
						continue;
					}
					
					double[] bottom_red_diff = new double[4];
					double[] bottom_green_diff = new double[4];
					double[] bottom_blue_diff = new double[4];
					
					double bottom_red_sum = 0.0, bottom_green_sum = 0.0, bottom_blue_sum = 0.0;

					for (int j = 0; j < 4; j++){
						bottom_red_diff[j] = Math.abs(red_hist[j] - neighbors[i].red_hist[j]);
						bottom_red_sum += (bottom_red_diff[j]* weights[j]);
						
						bottom_green_diff[j] = Math.abs(green_hist[j] - neighbors[i].green_hist[j]);
						bottom_green_sum += (bottom_green_diff[j]* weights[j]);
						
						bottom_blue_diff[j] = Math.abs(blue_hist[j] - neighbors[i].blue_hist[j]);
						bottom_blue_sum += (bottom_blue_diff[j]* weights[j]);
					} 

					neighbor_fitnesses[i] = bottom_red_sum + bottom_green_sum + bottom_blue_sum;
					total += neighbor_fitnesses[i];
					break;
				default:
					System.out.println("Something went wrong.");
					break;
			}
			//fitness = left_neighbor_fitness + top_neighbor_fitness + right_neighbor_fitness + bottom_neighbor_fitness;
		}
		
		fitness = 100 - (total * 8);
		
		//System.out.println("Member# " + id + " \nfitness: " + fitness + " || left_neighbor_fitness: "
				//+ neighbor_fitnesses[0] + " || top_neighbor_fitness: " + neighbor_fitnesses[1] + "\nright_neighbor_fitness: "
				//+ neighbor_fitnesses[2] + " || bottom_neighbor_fitness: " + neighbor_fitnesses[3]);
		//System.out.println("Member# " + id + " || red_hist: " + red_hist[0]);
	}
	
	*/
	

}
