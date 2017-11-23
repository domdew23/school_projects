import java.util.concurrent.ThreadLocalRandom;

public class Region{
	
	private double temp;
	private double C1;
	private double C2;
	private double C3;
	private double[] metals;
	private int x;
	private int y;
	private Region leftNeighbor;
	private Region topNeighbor;
	private Region rightNeighbor;
	private Region bottomNeighbor;
	private Region[] neighbors;
	private int neighborCount;
	public double red,green,blue;

	public Region(double C1, double C2, double C3, int x, int y){
		this.metals = new double[3];
		this.neighbors = new Region[4];
		init(C1, C2, C3);
		this.x = x;
		this.y = y;
		this.temp = 0.0;
		this.neighborCount = 0;
		this.red = 0;
		this.green = 0;
		this.blue = 0;
	}

	public Region compute(){
		double total = 0.0;
		double tmpTotal = 0.0;

		for (int i = 0; i < metals.length; i++){
			tmpTotal = 0.0;
			for (int j = 0; j < neighbors.length; j++){
				if (neighbors[j] != null){
					tmpTotal += (neighbors[j].getTemp() * neighbors[j].getMetals()[i]);
				}
			}
			tmpTotal *= metals[i];
			total /= neighborCount;
			total += tmpTotal;
		}
		Region retVal = this;
		retVal.setTemp(total);
		return retVal;
	}

	public void setNeighbors(Region left, Region top, Region right, Region bottom){
		leftNeighbor = left;
		neighbors[0] = left;
		topNeighbor = top;
		neighbors[1] = top;
		rightNeighbor = right;
		neighbors[2] = right;
		bottomNeighbor = bottom;
		neighbors[3] = bottom;
		
		if (leftNeighbor != null){
			neighborCount++;
		}
		if (topNeighbor != null){
			neighborCount++;
		}
		if (rightNeighbor != null){
			neighborCount++;
		}
		if (bottomNeighbor != null){
			neighborCount++;
		}
	}

	public void calcRGB(){
		//temp = temp + 273.15;
		//temp = temp/10;

    	if (temp > 10){ 
	       	red = 255; 
	        green = temp;
	        green = 9.4708025861 * Math.log(green) - 16.1195681661;
        	
        	if (temp >= 19){
            	blue = 0;
        	} else {
           		blue = temp-1;
            	blue = 13.5177312231 * Math.log(blue) - 30.0447927307;
        	}
    	} else {
	        red = temp - 6;
	        red = 60.698727446 * Math.pow(red, -0.1332047592);
	        
	        green = temp - 6;
	        green = 60.1221695283 * Math.pow(green, -0.0755148492 );

	        blue = 255;
    	}

    	if (red < 0 || Double.isNaN(red)){
    		red = 0;
    	} else if (red > 255){
    		red = 255;
    	}
    	if (green < 0 || Double.isNaN(green)){
    		green = 0;
    	} else if (green > 255){
    		green = 255;
    	}
    	if (blue < 0 || Double.isNaN(blue)){
    		blue = 0;
    	} else if (blue > 255){
    		blue = 255;
    	}
	}

	private void init(double C1, double C2, double C3){
		if (ThreadLocalRandom.current().nextDouble(1.0) < .5){
			this.C1 = (C1 * ThreadLocalRandom.current().nextDouble(.75, 1.0));
		} else {
			this.C1 = (C1 * ThreadLocalRandom.current().nextDouble(1.0, 1.25));
		}

		if (ThreadLocalRandom.current().nextDouble(1.0) < .5){
			this.C2 = (C2 * ThreadLocalRandom.current().nextDouble(.75, 1.0));
		} else {
			this.C2 = (C2 * ThreadLocalRandom.current().nextDouble(1.0, 1.25));
		}

		if (ThreadLocalRandom.current().nextDouble(1.0) < .5){
			this.C3 = (C3 * ThreadLocalRandom.current().nextDouble(.75, 1.0));
		} else {
			this.C3 = (C3 * ThreadLocalRandom.current().nextDouble(1.0, 1.25));
		}
		metals[0] = C1;
		metals[1] = C2;
		metals[2] = C2;	
	}

	public double[] getMetals(){
		return metals;
	}

	public double getTemp(){
		return temp;
	}

	public void setTemp(double temp){
		this.temp = (temp * ThreadLocalRandom.current().nextDouble(.5, 2.0));
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public String toString(){
		int leftX=-1,leftY=-1,topX=-1,topY=-1,rightX=-1,rightY=-1,bottomX=-1,bottomY=-1;
		if (leftNeighbor != null){
			leftX = leftNeighbor.getX();
			leftY = leftNeighbor.getY();
		}
		if (topNeighbor != null){
			topX = topNeighbor.getX();
			topY = topNeighbor.getY();
		}
		if (rightNeighbor != null){
			rightX = rightNeighbor.getX();
			rightY = rightNeighbor.getY();
		}
		if (bottomNeighbor != null){
			bottomX = bottomNeighbor.getX();
			bottomY = bottomNeighbor.getY();
		}

		return "x: " + x + " | y: " + y + " | temp: " + temp + " | C1: " + C1 + " | C2: " + C2 + " | C3: " + C3 + 
		"\nNeighbors:\nleft: " + leftX + "," + leftY + " | top: " + topX + "," + topY +" | right: " 
		+ rightX + "," + rightY + " | bottom: " + bottomX + "," + bottomY + "\nRGB:\nr: " + red + " | g: " + green + " | b: " + blue + "\n"; 
	}
}