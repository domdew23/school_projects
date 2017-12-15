import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.image.DataBufferInt;
import java.util.concurrent.ThreadLocalRandom;

public class GraphicsEngine extends Canvas implements Runnable{
	private Thread thread;
	private JFrame frame;
	Dimension size = new Dimension(Settings.WIDTH*Settings.SCALE, Settings.HEIGHT*Settings.SCALE);

	public GraphicsEngine(Control control){
		init();
	}

	private void init(){
		setPreferredSize(size);
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Alloy");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void run(){
		while (Settings.RUNNING){
			render();
		}
		stop();
	}

	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
			for (int y = 0; y < Settings.HEIGHT; y++){
				for (int x = 0; x < Settings.WIDTH; x++){
				draw(g, x, y);
			}
		}
		g.dispose();
		bs.show();
	}

	private void draw(Graphics g, int x, int y){
		Region r = Control.updatedAlloy[y][x];
		while (r == null){
			r = Control.updatedAlloy[y][x];
		}
		int red = (int) Math.round(r.red);
		int green = (int) Math.round(r.green);
		int blue = (int) Math.round(r.blue);
		g.setColor(new Color(red, green, blue));
		g.fillRect((x*Settings.SCALE), (y*Settings.SCALE), Settings.SCALE, Settings.SCALE);	
	}

	public synchronized void start(){
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}