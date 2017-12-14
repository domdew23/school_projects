public class Control {
	public static Region[][] A;
	public static Region[][] B;
	public static Region[][] updatedAlloy;
	public static double C1;
	public static double C2;
	public static double C3;
	
	public Control(){
		this.A = initAlloy();
		split(A);

		this.B = new Region[Settings.WIDTH][Settings.HEIGHT];
		this.updatedAlloy = A;
	}

	private void split(Region[][] array){		
		for (Region[] chunk : array){

		}
			
	}

	private Region[][] initAlloy(){
		Region[][] alloy = new Region[Settings.HEIGHT][Settings.WIDTH];

		for (int y = 0; y < Settings.HEIGHT; y++){
			for (int x = 0; x < Settings.WIDTH; x++){
				Region r = new Region(x, y);
				alloy[y][x] = r;
				
				if (x == 0 && y == 0){
					r.setTemp(Settings.S);
				} else if (x == Settings.WIDTH-1 && y == Settings.HEIGHT-1){
					r.setTemp(Settings.T);
				} else {
					r.setTemp(0.0);
				}
				r.calcRGB();
			}
		}
		updateNeighbors(alloy);
		return alloy;
	}

	public static void updateNeighbors(Region[][] alloy){
		
		for (int y = 0; y < Settings.HEIGHT; y++){
			for (int x = 0; x < Settings.WIDTH; x++){
				if (alloy[y][x] == null){
					continue;
				}
				Region left=null,top=null,right=null,bottom=null;
				if (x-1 >= 0){
					left = alloy[y][x-1];
				}
				if (x+1 < Settings.WIDTH){
					right = alloy[y][x+1];
				}
				if (y-1 >= 0){
					top = alloy[y-1][x];
				}
				if (y+1 < Settings.HEIGHT){
					bottom = alloy[y+1][x];
				}
				alloy[y][x].setNeighbors(left, top, right, bottom);
				System.out.println(alloy[y][x]);
			}
		}
	}

	public static void addPart(Region[][] part){
		double m = 0.0;
		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				if (part[i][j] != null){
					updatedAlloy[i][j] = part[i][j];
				}
			}
		}
	}

	public static Region[][] getUpdatedAlloy(){
		return updatedAlloy;
	}
}