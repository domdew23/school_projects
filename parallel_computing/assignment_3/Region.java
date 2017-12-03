import java.util.concurrent.ThreadLocalRandom;

public class Region{
	
	private double temp;
	private double p1;
	private double p2;
	private double p3;
	private double[] percentages;
	private int x;
	private int y;
	private Region leftNeighbor;
	private Region topNeighbor;
	private Region rightNeighbor;
	private Region bottomNeighbor;
	private Region[] neighbors;
	private int neighborCount;
	public double red,green,blue;

	public Region(int x, int y){
		this.percentages = new double[3];
		this.neighbors = new Region[4];
		init();
		this.x = x;
		this.y = y;
		this.temp = 0.0;
		this.neighborCount = 0;
		this.red = 0;
		this.green = 0;
		this.blue = 0;
	}

	public Region compute(){
		boolean isTopLeft = (x == 0 && y == 0);
		boolean isBottomRight = (x == Settings.WIDTH - 1 && y == Settings.HEIGHT - 1);
		
		if (isTopLeft || isBottomRight || !(hasNonZeroNeighbor()) || Double.isInfinite(temp)){
			Region retVal = new Region(this.x, this.y);
			if (Double.isInfinite(temp)){
				retVal.setTemp(100000.0);
			} else {
				retVal.setTemp(this.temp);
			}
			retVal.calcRGB();
			return retVal;
		}

		double total = 0.0;
		double tmpTotal = 0.0;

		for (int i = 0; i < Settings.METALS.length; i++){
			tmpTotal = 0.0;
			for (int j = 0; j < neighbors.length; j++){
				if (neighbors[j] != null){
					tmpTotal += (neighbors[j].getTemp() * neighbors[j].getMetals()[i]);
				}
			}
			tmpTotal *= Settings.METALS[i];
			total /= neighborCount;
			total += tmpTotal;
		}

		Region retVal = new Region(this.x, this.y);
		retVal.setTemp(total);
		retVal.calcRGB();
		return retVal;
	}

	private boolean hasNonZeroNeighbor(){
		for (int i = 0; i < neighbors.length; i++){
			if (neighbors[i] != null){
				if (neighbors[i].getTemp() != 0.0){
					return true;
				}
			}
		}
		return false;
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

	private void init(){
		if (ThreadLocalRandom.current().nextDouble(1.0) < .5){
			this.p1 = (Settings.C1 * ThreadLocalRandom.current().nextDouble(.75, 1.0));
		} else {
			this.p1 = (Settings.C1 * ThreadLocalRandom.current().nextDouble(1.0, 1.25));
		}

		if (ThreadLocalRandom.current().nextDouble(1.0) < .5){
			this.p2 = (Settings.C2 * ThreadLocalRandom.current().nextDouble(.75, 1.0));
		} else {
			this.p2 = (Settings.C2 * ThreadLocalRandom.current().nextDouble(1.0, 1.25));
		}

		if (ThreadLocalRandom.current().nextDouble(1.0) < .5){
			this.p3 = (Settings.C3 * ThreadLocalRandom.current().nextDouble(.75, 1.0));
		} else {
			this.p3 = (Settings.C3 * ThreadLocalRandom.current().nextDouble(1.0, 1.25));
		}
		percentages[0] = p1;
		percentages[1] = p2;
		percentages[2] = p3;
	}

	public double[] getMetals(){
		return percentages;
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
		double leftTemp=-1,topTemp=-1,rightTemp=-1,bottomTemp=-1;
		if (leftNeighbor != null){
			leftX = leftNeighbor.getX();
			leftY = leftNeighbor.getY();
			leftTemp = leftNeighbor.getTemp();
		}
		if (topNeighbor != null){
			topX = topNeighbor.getX();
			topY = topNeighbor.getY();
			topTemp = topNeighbor.getTemp();
		}
		if (rightNeighbor != null){
			rightX = rightNeighbor.getX();
			rightY = rightNeighbor.getY();
			rightTemp = rightNeighbor.getTemp();
		}
		if (bottomNeighbor != null){
			bottomX = bottomNeighbor.getX();
			bottomY = bottomNeighbor.getY();
			bottomTemp = bottomNeighbor.getTemp();
		}

		return "x: " + x + " | y: " + y + " | temp: " + temp + "\nNeighbors:\nleft: " + leftX + "," + leftY
		+ "| " + leftTemp + " | top: " + topX + "," + topY + "| " + topTemp +" | right: " + rightX + "," + 
		rightY + " | " + rightTemp + " | bottom: " + bottomX + "," + bottomY +" | " + bottomTemp + 
		"\nRGB:\nr: " + red + " | g: " + green + " | b: " + blue + "\n"; 
	}
}