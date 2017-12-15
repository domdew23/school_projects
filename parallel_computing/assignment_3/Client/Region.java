import java.util.concurrent.ThreadLocalRandom;
import java.io.Serializable;

public class Region implements Serializable {
	
	private double temp;
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
		this.calcRGB();
	}

	private void handleCorner(Region retVal, double cornerTemp){
		retVal.temp = cornerTemp;
		retVal.red = this.red;
		retVal.green = this.green;
		retVal.blue = this.blue;
	}

	public Region compute(){
		boolean isTopLeft = (x == 0 && y == 0);
		boolean isBottomRight = (x == Settings.WIDTH - 1 && y == Settings.HEIGHT - 1);

		if (isTopLeft){
			Region retVal = new Region(this.x, this.y);
			handleCorner(retVal, Settings.S);
			return retVal;
		} else if (isBottomRight){
			Region retVal = new Region(this.x, this.y);
			handleCorner(retVal, Settings.T);
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
			tmpTotal /= neighborCount;
			total += tmpTotal;
		}

		Region retVal = new Region(this.x, this.y);
		retVal.setNeighbors(this.leftNeighbor, this.topNeighbor, this.rightNeighbor, this.bottomNeighbor);
		retVal.setTemp(total);
		retVal.calcRGB();
		//if (x == Settings.WIDTH/2 && y == Settings.HEIGHT/2) System.out.println(retVal.temp + " - " + retVal.x + ", " + retVal.y);
		return retVal;
	}

	private boolean hasNonZeroNeighbor(){
		for (int i = 0; i < neighbors.length; i++){
			if (neighbors[i] != null){
				if (neighbors[i].getTemp() != 0){
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
    	if (temp > Math.log(Settings.MAX) * 10){ 
	       	red = 255;
	       	green = temp;

	       	green = 9.4708025861 * Math.log(green) - 16.1195681661;
        	
        	if (temp >= Math.log(Settings.MAX)){
            	blue = 0;
        	} else {
           		blue = temp;
            	blue = 13.5177312231 * Math.log(blue) - 30.0447927307;
        	}
    	} else {
	        red = temp - 6;
	        red = 30.698727446 * Math.pow(red, -0.1332047592);
	        
	        green = temp - 6;
	        green = 30.1221695283 * Math.pow(green, -0.0755148492);

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

	public int getRGB(){
		return (int) Math.round(red + green + blue);
	}

	public String rgb(){
		return ("R: " + red + " G: " + green + " B: " + blue);
	}

	private void init(){
		double p1, p2, p3;
		p1 = ThreadLocalRandom.current().nextDouble(0, 1.0);		
		p2 = ThreadLocalRandom.current().nextDouble(0, 1.0-p1);
		p3 = ThreadLocalRandom.current().nextDouble(0, 1.0-(p1+p2));

		percentages[0] = p1;
		percentages[1] = p2;
		percentages[2] = p3;
		double total = (percentages[0] + percentages[1] + percentages[2]);
		
		if (total < 1){
			double diff = Math.abs(total - 1);
			percentages[2] += diff;
		}
		total = (percentages[0] + percentages[1] + percentages[2]);
	}

	public double[] getMetals(){
		return percentages;
	}

	public double getTemp(){
		return temp;
	}

	public void setTemp(double temp){
		this.temp = (temp * ThreadLocalRandom.current().nextDouble(.8, 2.0));
		
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public String toString(){
		if (1/2 == 1){
			return "x: " + x + " | y: " + y + " " + rgb();
		}
		
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