
class Pixel{
	int x, y, loc, index = 0;
	float fitness;
	Pixel[] neighbors;

	float rgb;
	public Pixel(int x, int y, int loc, float rgb){
		this.x = x;
		this.y = y;
		this.rgb = rgb;
		this.loc = loc;
		this.neighbors = new Pixel[8];
		//System.out.print("x: " + x + " || y: " + y + " || + color: " + rgb);
	}

	public void add_neighbor(Pixel p){
		neighbors[index] = p;
		index++;
	}

	public void print_neighbors(){
		for (int i = 0; i < neighbors.length; i++){
			System.out.println("neighbors["+i+"] rgb: " + neighbors[i].rgb);
		}
	}

	public void print_pixel(){
		System.out.println("Location: " + loc + " || x: " + x + " || y: " + y + " || RGB: " + rgb + " || fitness: " + fitness);
	}

	public void set_fitness(){
		for (int i = 0; i < neighbors.length; i++){
			float diff = Math.abs(rgb - neighbors[i].rgb);
			fitness += diff;
		}
	}
}