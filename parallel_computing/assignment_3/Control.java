public class Control {
	public static Region[][] A;
	public static Region[][] B;
	public static Region[][] updatedAlloy;
	public static double C1;
	public static double C2;
	public static double C3;
	public static double MAX;
	
	public Control(){
		this.A = initAlloy();
		this.B = new Region[Settings.WIDTH][Settings.HEIGHT];
		this.updatedAlloy = A;
	}

	private Region[][] initAlloy(){
		Region[][] alloy = new Region[Settings.WIDTH][Settings.HEIGHT];

		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				Region r = new Region(i, j);
				alloy[i][j] = r;
				if (i == 0 && j == 0){
					r.setTemp(Settings.S);
				} else if (i == Settings.WIDTH-1 && j == Settings.HEIGHT-1){
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
		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				if (alloy[i][j] == null){
					continue;
				}
				Region left=null,top=null,right=null,bottom=null;
				if (i-1 >= 0){
					left = alloy[i-1][j];
				}
				if (i+1 < Settings.WIDTH){
					right = alloy[i+1][j];
				}
				if (j-1 >= 0){
					top = alloy[i][j-1];
				}
				if (j+1 < Settings.HEIGHT){
					bottom = alloy[i][j+1];
				}
				alloy[i][j].setNeighbors(left, top, right, bottom);
			}
		}
	}

	public static void addPart(Region[][] part){
		double m = 0.0;
		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				if (part[i][j] != null){
					updatedAlloy[i][j] = part[i][j];
					m = Math.max(m, part[i][j].getTemp());
				}
			}
		}
		MAX = m;
	}

	public synchronized static Region[][] getUpdatedAlloy(){
		return updatedAlloy;
	}
}