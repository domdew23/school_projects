public class Control {
	public static Region[][] A;
	public static Region[][] B;
	public static Region[][] updatedAlloy;
	public static double C1;
	public static double C2;
	public static double C3;
	
	public Control(){
		this.A = initAlloy();

		//this.B = new Region[Settings.HEIGHT][Settings.WIDTH];
		//this.updatedAlloy = A;
	}

	public Region[][][] split(Region[][] array){		
		Chunk[] allChunks = new Chunk[Settings.CLIENTS];
		int offset = 0;

		for (int i = 0; i < Settings.CLIENTS; i++,offset+=Settings.CHUNK_SIZE){
			Chunk chunk = new Chunk();
			Region[][] tmp = new Region[Settings.CHUNK_SIZE][Settings.WIDTH];
			for (int y = offset, z = 0; z < Settings.CHUNK_SIZE; y++,z++){
				for (int x = 0; x < Settings.WIDTH; x++){	
					tmp[z][x] = array[y][x];
				}
			}
			//allChunks[i] = chunk;
			boolean hasTop = (i - 1 > 0) ? true : false;
			boolean hasBottom = (i + 1 < Settings.HEIGHT) ? true : false;

			chunk.setChunk(tmp, hasTop, hasBottom);
			allChunks[i] = chunk;
		}
		return allChunks;	
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
			}
		}
	}

	public static void addPart(Region[][] part){
		double m = 0.0;
		for (int y = 0; y < Settings.HEIGHT;y++){
			for (int x = 0; x < Settings.WIDTH; x++){
				if (part[y][x] != null){
					updatedAlloy[y][x] = part[y][x];
				}
			}
		}
	}

	public static Region[][] getUpdatedAlloy(){
		return updatedAlloy;
	}
}