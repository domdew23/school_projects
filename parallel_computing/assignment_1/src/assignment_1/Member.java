package assignment_1;
import processing.core.PImage;

public class Member{
	int x=0, y=0; 
	
	double red_hist1 = 0; // 0 - 63
	double red_hist2 = 0; // 64 - 127
	double red_hist3 = 0; // 128 - 191
	double red_hist4 = 0; // 192 - 255

	double green_hist1 = 0; // 0 - 63
	double green_hist2 = 0; // 64 - 127
	double green_hist3 = 0; // 128 - 191
	double green_hist4 = 0; // 192 - 255
	
	double blue_hist1 = 0; // 0 - 63
	double blue_hist2 = 0; // 64 - 127
	double blue_hist3 = 0; // 128 - 191
	double blue_hist4 = 0; // 192 - 255

	Member[] neighbors; // [0] left, [1] top, [2] right, [3] bottom
	int count;
	double fitness;
	double[] neighbor_fitnesses;
	PImage img;
	boolean needs_to_swap = false;
	int id = 0;
	
	Member(int id, int x, int y, PImage img){
		this.id = id;
		this.x = x;
		this.y = y;
		this.fitness = 0;
		this.img = img;
		neighbor_fitnesses = new double[4];
		neighbors = new Member[4];
		count = 0;
	}
	
	public void evaluate_fitness(){
		double total = 0.0;
		for (int i = 0; i < neighbors.length; i++){
			switch(i){
				case 0:
					//left
					if (neighbors[i] == null){
						neighbor_fitnesses[i]  = -1.0;
						break;
					}
					double left_red_1_diff = Math.abs(red_hist1 - neighbors[i].red_hist1);
					double left_red_2_diff =  Math.abs(red_hist2 - neighbors[i].red_hist2);
					double left_red_3_diff =  Math.abs(red_hist3 - neighbors[i].red_hist3);
					double left_red_4_diff =  Math.abs(red_hist4 - neighbors[i].red_hist4);
					double left_red_sum =  left_red_1_diff + left_red_2_diff + left_red_3_diff + left_red_4_diff;

					double left_green_1_diff =  Math.abs(green_hist1 - neighbors[i].green_hist1);
					double left_green_2_diff =  Math.abs(green_hist2 - neighbors[i].green_hist2);
					double left_green_3_diff =  Math.abs(green_hist3 - neighbors[i].green_hist3);
					double left_green_4_diff =  Math.abs(green_hist4 - neighbors[i].green_hist4);	
					double left_green_sum = left_green_1_diff + left_green_2_diff + left_green_3_diff + left_green_4_diff;
							
					double left_blue_1_diff =  Math.abs(blue_hist1 - neighbors[i].blue_hist1);
					double left_blue_2_diff =  Math.abs(blue_hist2 - neighbors[i].blue_hist2);
					double left_blue_3_diff =  Math.abs(blue_hist3 - neighbors[i].blue_hist3);
					double left_blue_4_diff =  Math.abs(blue_hist4 - neighbors[i].blue_hist4);
					double left_blue_sum = left_blue_1_diff + left_blue_2_diff + left_blue_3_diff + left_blue_4_diff;
					
					neighbor_fitnesses[0] = left_red_sum + left_green_sum + left_blue_sum;
					total += neighbor_fitnesses[0] ;
					break;
				
				case 1:
					//top
					if (neighbors[i] == null){
						neighbor_fitnesses[i]  = -1.0;
						continue;
					}
					double top_red_1_diff = Math.abs(red_hist1 - neighbors[i].red_hist1);
					double top_red_2_diff =  Math.abs(red_hist2 - neighbors[i].red_hist2);
					double top_red_3_diff =  Math.abs(red_hist3 - neighbors[i].red_hist3);
					double top_red_4_diff =  Math.abs(red_hist4 - neighbors[i].red_hist4);
					double top_red_sum = top_red_1_diff + top_red_2_diff + top_red_3_diff + top_red_4_diff; 
					
					double top_green_1_diff = Math.abs(green_hist1 - neighbors[i].green_hist1);
					double top_green_2_diff = Math.abs(green_hist2 - neighbors[i].green_hist2);
					double top_green_3_diff = Math.abs(green_hist3 - neighbors[i].green_hist3);
					double top_green_4_diff = Math.abs(green_hist4 - neighbors[i].green_hist4);	
					double top_green_sum = top_green_1_diff + top_green_2_diff + top_green_3_diff + top_green_4_diff;
					
					double top_blue_1_diff =  Math.abs(blue_hist1 - neighbors[i].blue_hist1);
					double top_blue_2_diff =  Math.abs(blue_hist2 - neighbors[i].blue_hist2);
					double top_blue_3_diff =  Math.abs(blue_hist3 - neighbors[i].blue_hist3);
					double top_blue_4_diff =  Math.abs(blue_hist4 - neighbors[i].blue_hist4);
					double top_blue_sum = top_blue_1_diff + top_blue_2_diff + top_blue_3_diff + top_blue_4_diff;
					
					neighbor_fitnesses[1] = top_red_sum + top_green_sum + top_blue_sum;
					total += neighbor_fitnesses[1];
					break;
					
				case 2:
					//right
					if (neighbors[i] == null){
						neighbor_fitnesses[i] = -1.0;
						continue;
					}
					double right_red_1_diff = Math.abs(red_hist1 - neighbors[i].red_hist1);
					double right_red_2_diff = Math.abs(red_hist2 - neighbors[i].red_hist2);
					double right_red_3_diff = Math.abs(red_hist3 - neighbors[i].red_hist3);
					double right_red_4_diff = Math.abs(red_hist4 - neighbors[i].red_hist4);
					double right_red_sum = right_red_1_diff + right_red_2_diff + right_red_3_diff + right_red_4_diff; 

					double right_green_1_diff = Math.abs(green_hist1 - neighbors[i].green_hist1);
					double right_green_2_diff = Math.abs(green_hist2 - neighbors[i].green_hist2);
					double right_green_3_diff = Math.abs(green_hist3 - neighbors[i].green_hist3);
					double right_green_4_diff = Math.abs(green_hist4 - neighbors[i].green_hist4);	
					double right_green_sum = right_green_1_diff + right_green_2_diff + right_green_3_diff + right_green_4_diff; 

					double right_blue_1_diff = Math.abs(blue_hist1 - neighbors[i].blue_hist1);
					double right_blue_2_diff = Math.abs(blue_hist2 - neighbors[i].blue_hist2);
					double right_blue_3_diff = Math.abs(blue_hist3 - neighbors[i].blue_hist3);
					double right_blue_4_diff = Math.abs(blue_hist4 - neighbors[i].blue_hist4);
					double right_blue_sum = right_blue_1_diff + right_blue_2_diff + right_blue_3_diff + right_blue_4_diff; 

					neighbor_fitnesses[i] = right_red_sum + right_green_sum + right_blue_sum;
					total += neighbor_fitnesses[i];
					break;
				
				case 3:
					//bottom
					if (neighbors[i] == null){
						neighbor_fitnesses[i]= -1.0;
						continue;
					}
					double bottom_red_1_diff = Math.abs(red_hist1 - neighbors[i].red_hist1);
					double bottom_red_2_diff = Math.abs(red_hist2 - neighbors[i].red_hist2);
					double bottom_red_3_diff = Math.abs(red_hist3 - neighbors[i].red_hist3);
					double bottom_red_4_diff = Math.abs(red_hist4 - neighbors[i].red_hist4);
					double bottom_red_sum = bottom_red_1_diff + bottom_red_2_diff + bottom_red_3_diff + bottom_red_4_diff; 

					double bottom_green_1_diff = Math.abs(green_hist1 - neighbors[i].green_hist1);
					double bottom_green_2_diff = Math.abs(green_hist2 - neighbors[i].green_hist2);
					double bottom_green_3_diff = Math.abs(green_hist3 - neighbors[i].green_hist3);
					double bottom_green_4_diff = Math.abs(green_hist4 - neighbors[i].green_hist4);	
					double bottom_green_sum = bottom_green_1_diff + bottom_green_2_diff + bottom_green_3_diff + bottom_green_4_diff; 

					double bottom_blue_1_diff = Math.abs(blue_hist1 - neighbors[i].blue_hist1);
					double bottom_blue_2_diff = Math.abs(blue_hist2 - neighbors[i].blue_hist2);
					double bottom_blue_3_diff = Math.abs(blue_hist3 - neighbors[i].blue_hist3);
					double bottom_blue_4_diff = Math.abs(blue_hist4 - neighbors[i].blue_hist4);
					double bottom_blue_sum = bottom_blue_1_diff + bottom_blue_2_diff + bottom_blue_3_diff + bottom_blue_4_diff; 

					neighbor_fitnesses[i] = bottom_red_sum + bottom_green_sum + bottom_blue_sum;
					total += neighbor_fitnesses[i];
					break;
				default:
					System.out.println("Something went wrong.");
					break;
			}
			
			//fitness = left_neighbor_fitness + top_neighbor_fitness + right_neighbor_fitness + bottom_neighbor_fitness;
		}
		
		
		fitness = 100 - (total * 5);
		
		//System.out.println("Member# " + id + " \nfitness: " + fitness + " || left_neighbor_fitness: "
		//		+ neighbor_fitnesses[0] + " || top_neighbor_fitness: " + neighbor_fitnesses[1] + "\nright_neighbor_fitness: "
		//		+ neighbor_fitnesses[2] + " || bottom_neighbor_fitness: " + neighbor_fitnesses[3]);
	}
	
	public void add_neighbor(Member member){
		neighbors[count] = member;
		count++;
	}
	
	public void reset(){
		count = 0;
		fitness = 0;
	}

}
