import java.util.Arrays;

class Population{
	Pixel[] pixels;

	Population(Pixel[] pixels){
		this.pixels = pixels;
		Arrays.sort(pixels, (a, b) -> a.fitness - b.fitness);
	}

	public void print_pop(){
		for (int i = 0; i < pixels.length; i++){
			pixels[i].print_pixel();
		}
	}
}