import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class GraphicsModule extends JPanel {
	private static int width = Settings.WIDTH;
	private static int height = width * 16 / 9;
	int columns = width/2;
	private static int scale = Settings.SCALE;
	private Region[][] alloy;

	public GraphicsModule(Region[][] alloy){
		//this.width = width;
		//this.height = height;
		//this.scale = scale;
		this.alloy = alloy;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.GRAY);

		for (int i = 0; i < width; i++){
			for (int j = 0; j < columns; j++){
				Region r = alloy[i][j];
				r.calcRGB();
				int red = (int) Math.round(r.red);
				int green = (int) Math.round(r.green);
				int blue = (int) Math.round(r.blue);
				//System.out.println(r);
				g.setColor(new Color(red, green, blue));
				g.fillRect((i*scale), (j*scale), scale, scale);
			}
		}
	}
}