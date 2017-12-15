public class Control {
	public static Region[][] A;
	public static Region[][] updatedAlloy;
	public static Chunk[] allChunks;
	
	public Control(){
		this.A = initAlloy();
		this.updatedAlloy = A;
	}

	public Chunk[] split(Region[][] array){
		Chunk[] tmpAllChunks = new Chunk[Settings.CLIENTS];
		int offset = 0;

		for (int i = 0; i < Settings.CLIENTS; i++,offset+=Settings.CHUNK_SIZE){
			Region[][] tmp = new Region[Settings.CHUNK_SIZE][Settings.WIDTH];
			Chunk chunk = new Chunk();
			for (int y = offset, z = 0; z < Settings.CHUNK_SIZE; y++,z++){
				for (int x = 0; x < Settings.WIDTH; x++){	
					tmp[z][x] = array[y][x];
				}
			}
			chunk.setElements(tmp);
			tmpAllChunks[i] = chunk;
		}
		updateEdges(tmpAllChunks);
		return tmpAllChunks;	
	}

	public static void updateEdges(Chunk[] chunks){
		boolean topSet = false, bottomSet = false;
		int offset = 0;

		for (int i = 0; i < chunks.length; i++){
			Region[] top = (i - 1 >= 0) ? chunks[i-1].elements[Settings.CHUNK_SIZE-1] : null;
			Region[] bottom = (i + 1 < Settings.CLIENTS) ? chunks[i+1].elements[0] : null;
		
			chunks[i].setEdges(top, bottom);
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
		for (int y = 0; y < alloy.length; y++){
			for (int x = 0; x < alloy[y].length; x++){
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
		for (int y = 0; y < part.length; y++){
			for (int x = 0; x < part[y].length; x++){
				if (part[y][x] != null){
					int newY = part[y][x].getY();
					int newX = part[y][x].getX(); 
					updatedAlloy[newY][newX] = part[y][x];
				}
			}
		}
	}

	public static void update(){
		for (int i = 0; i < allChunks.length; i++){
			addPart(allChunks[i].elements);
		}
		updateNeighbors(updatedAlloy);

		for (int i = 0,offset=0; i < allChunks.length; i++,offset+=Settings.CHUNK_SIZE){	
			for (int y = offset, z = 0; z < Settings.CHUNK_SIZE; y++,z++){
				for (int x = 0; x < Settings.WIDTH; x++){	
					allChunks[i].elements[z][x] = updatedAlloy[y][x];
				}
			}
		}

		updateEdges(allChunks);
		for (int i = 0; i < allChunks.length; i++){
			//System.out.println("CHUNK " + i + allChunks[i]);
		}
		//if (1/1 == 1) System.exit(0);
	}

	public static Region[][] getUpdatedAlloy(){
		return updatedAlloy;
	}
}