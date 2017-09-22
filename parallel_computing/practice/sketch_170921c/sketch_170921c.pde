
//size(1600, 900);
//background(0);

/*
loadPixels();
int loc;
for (int x = 0; x < width-1; x++){
  loc = x+(200*width);
  if (x % 2 == 0){
    pixels[loc] = color(0, 0, 0);
    System.out.println("original: " + color(pixels[loc]));
  } else {
    pixels[loc] = color(255, 255, 255  );
    System.out.println("right neighbor: " + color(pixels[loc]));
  }
}

int orig = 50 + (200*width);
int right = 51 + (200*width);
int left = 49 + (200*width);
int top = 51 + (199*width);
int bottom = 49 + (201*width);
int top_right = 51 + (199*width);
int top_left = 49 + (199*width);
int bottom_right = 51 + (201*width);
int bottom_left = 49 + (201*width);

float c1 = color(pixels[orig]);
float c_right = color(pixels[right]);
float c_left = color(pixels[left]);
float c_top = color(pixels[top]);
float c_bottom = color(pixels[bottom]);
float c_top_right = color(pixels[top_right]);
float c_top_left = color(pixels[top_left]);
float c_bottom_right = color(pixels[bottom_right]);
float c_bottom_left = color(pixels[bottom_left]);

float diff_1 = abs(c1 - c_right);
float diff_2 = abs(c1 - c_left);
float diff_3 = abs(c1 - c_top);
float diff_4 = abs(c1 - c_bottom);
float diff_5 = abs(c1 - c_top_right);
float diff_6 = abs(c1 - c_top_left);
float diff_7 = abs(c1 - c_bottom_right);
float diff_8 = abs(c1 - c_bottom_left);

float total = 0;
total = diff_1 + diff_2 + diff_3 + diff_4 + diff_5 + diff_6 + diff_7 + diff_8;

System.out.println("ORIGINAL: "  + c1 + " || left: " + c_left + " || top_left: " + c_top_left);
System.out.println("top: "  + c_top + " || top_right: " + c_top_right + " || right: " + c_right);
System.out.println("bottom_right: " + c_bottom_right + " || bottom: " + c_bottom + " || bottom_left: " + c_bottom_left);
System.out.println("diff: " + total);

updatePixels();
*/

/*loadPixels();
for (int i = 0; i < pixels.length; i++){
  pixels[i] = color(random(255), random(255), random(255));
  // x + (y * width) = index of pixels array 
}*/

/*for (int x = 0; x < width; x++){
  for (int y = 0; y < height; y++){
    pixels[x+(y*width)] = color(0, y/2, 0);
  }
}*/
// finding edge -> look for pixels different then their neighbors

//updatePixels();


PImage pic;
void setup(){
   size(1600, 900);
   pic = loadImage("pic.jpg");
}


void draw(){
  //image(pic, 0, 0);
  loadPixels();
  pic.loadPixels();
  for (int x = 0; x < width-1; x++){
    for (int y = 0; y < height-1; y++){
      int loc = x+(y*width);
      /*int right_neighbor = (x + 1) + y*width;
      float b5 = 0.0;
      if (x != 0){
        int left_neighbor = (x - 1) + y*width;
        b5 = color(pic.pixels[left_neighbor]);
      }
      float b3 = 0.0;
      if (y!= 0){
        int top_neighbor = x + ((y-1)*width);
        b3 = color(pic.pixels[top_neighbor]);
      }
      
      int bottom_neighbor = x + ((y+1)*width);
      float b1 = color(pic.pixels[loc]);
      float b2 = color(pic.pixels[right_neighbor]);
      float b4 = color(pic.pixels[bottom_neighbor]);
      
      float diff = abs(b1 - b2);
      pixels[loc] = color(diff);
      
      System.out.println("oringal: " + b1 + " || right: " + b2 + " || left: " + b5);
      /*
      float r = red(pic.pixels[loc]);
      float g = green(pic.pixels[loc]);
      float b = blue(pic.pixels[loc]);
      // copy pixels from image to canvas
      pixels[loc] = color(r, g*2, b);
      */
      float b = brightness(pic.pixels[loc]);
      //System.out.println(color(pic.pixels[loc]));
      if (b > mouseX){
        pixels[loc] = color(255);
      } else {
        pixels[loc] = color(0);
      }
    }
  }
  updatePixels();
}