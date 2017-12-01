public class Control {
	public static Region[][] A;
	public static Region[][] B;
	public static double C1;
	public static double C2;
	public static double C3;
	
	public Control(){
		this.A = initAlloy();
		this.B = new Region[Settings.WIDTH][Settings.HEIGHT];
	}

	private Region[][] initAlloy(){
		Region[][] alloy = new Region[Settings.WIDTH][Settings.HEIGHT];

		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				Region r = new Region(Settings.C1, Settings.C2, Settings.C3, i, j);
				alloy[i][j] = r;
				r.setTemp(0.0);
			}
		}

		alloy[0][0].setTemp(Settings.S);
		alloy[Settings.WIDTH-1][Settings.HEIGHT-1].setTemp(Settings.T);
		
		updateNeighbors(alloy);
		return alloy;
	}

	public synchronized static void updateNeighbors(Region[][] alloy){
		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
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
				if (alloy[i][j] == null){
					System.out.println("i was null: " + " i: " + i + " | j: " + j);	
				}
				alloy[i][j].setNeighbors(left, top, right, bottom);
			}
		}
	}
}