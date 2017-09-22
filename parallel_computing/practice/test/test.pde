Pixel[] all_pixels;
int index = 0;
void setup(){
  size(20,20);
  background(0);
  all_pixels = new Pixel[20*20];
  get_pixels();
  Population pop = new Population(all_pixels);
  pop.print_pop();
}

void draw(){

}

void get_pixels(){
  loadPixels();
  pixels[6] = color(255,255,255);
  for (int y = 0; y < height; y++){
    for (int x = 0; x < width; x++){
      int loc = x + (y * width);
      pixels[loc] = color(random(255), random(255), random(255));
      Pixel pixel = new Pixel(x, y, loc, color(pixels[loc]));
      
      pixel.add_neighbor(get_neighbor(x, y, -1, 1)); //bottom left   
      pixel.add_neighbor(get_neighbor(x, y, -1, 0)); //left
      pixel.add_neighbor(get_neighbor(x, y, -1, -1)); //top left   
      pixel.add_neighbor(get_neighbor(x, y, 0, -1)); //top   
      pixel.add_neighbor(get_neighbor(x, y, 1, -1)); //top right   
      pixel.add_neighbor(get_neighbor(x, y, 1, 0)); //right   
      pixel.add_neighbor(get_neighbor(x, y, -1, 1)); //bottom right   
      pixel.add_neighbor(get_neighbor(x, y, 0, 1)); //bottom
      
      all_pixels[index] = pixel;
      pixel.set_fitness();
      index++;
    }
  }
  updatePixels();
}

Pixel get_neighbor(int x, int y, int x_shift, int y_shift){
  Pixel pix;
  x += x_shift;
  y += y_shift;
  int loc = x + (y * width);
  try{
    pix = new Pixel(x, y, loc, color(pixels[loc]));
  } catch (Exception e){
    x -= x_shift;
    y -= y_shift;
    loc = x + (y * width);
    pix = new Pixel(x, y, loc, color(pixels[loc]));
  }
  return pix;
}