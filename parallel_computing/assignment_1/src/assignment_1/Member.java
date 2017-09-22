package assignment_1;

import java.util.Arrays;

public class Member {
	int[] pixels;
	float fitness;
	int width, height;
	int id = 0;
	
	Member(int id, int[] pixels, int width, int height){
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		set_fitness();
		this.id = id;
	}
	
	public void print_first_ten(){
		System.out.println("Member #" + id + ":");
		for (int i = 0; i < 10; i++){
			System.out.println(pixels[i]);
		}
		System.out.println("---------");
	}
	
	public void set_fitness(){
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				  int[] neighbors = new int[8];
				  int loc = x + (y * width);
			      neighbors[0] = get_neighbor(x, y, -1, 1); //bottom left   
			      neighbors[1] = get_neighbor(x, y, -1, 0); //left
			      neighbors[2] = get_neighbor(x, y, -1, -1); //top left   
			      neighbors[3] = get_neighbor(x, y, 0, -1); //top   
			      neighbors[4] = get_neighbor(x, y, 1, -1); //top right   
			      neighbors[5] =  get_neighbor(x, y, 1, 0); //right   
			      neighbors[6] = get_neighbor(x, y, -1, 1); //bottom right   
			      neighbors[7] = get_neighbor(x, y, 0, 1); //bottom
			      for (int i = 0; i < neighbors.length; i++){
				      float diff = Math.abs(pixels[loc] - neighbors[i]);
				      fitness += diff;
			      }
			}
		}
		fitness = (fitness/pixels.length);
	}
	
	private int get_neighbor(int x, int y, int x_shift, int y_shift){
	  x += x_shift;
	  y += y_shift;
	  int loc = x + (y * width);
	  try{
		    return pixels[loc];
		  } catch (Exception e){
		    x -= x_shift;
		    y -= y_shift;
		    loc = x + (y * width);
		    return pixels[loc];
		  }
	}

}
