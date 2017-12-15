import java.io.Serializable;

public class Chunk implements Serializable {
	public boolean hasTopEdge;
	public boolean hasBottomEdge;
	public Region[] topEdge;
	public Region[] bottomEdge;
	public Region[][] elements;

	public Chunk(){

	}

	public void setElements(Region[][] elements){
		this.elements = elements;
	}

	public void setEdges( Region[] topEdge, Region[] bottomEdge){
		this.topEdge = topEdge;
		this.bottomEdge = bottomEdge;

		hasTopEdge = (topEdge != null) ? true : false;
		hasBottomEdge = (bottomEdge != null) ? true : false;
	}

	public String toString(){
		String str = "";
		for (int y = 0; y < elements.length; y++) { 
			for (int x = 0; x < elements[y].length; x++){
				str += ("\n" + elements[y][x]);
			}
		}
		return "top: " + hasTopEdge + " | bottom: " + hasBottomEdge + str;
	}
}