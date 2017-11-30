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
	BufferedImage image = new BufferedImage(Settings.WIDTH, Settings.HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private Screen screen;

	public GraphicsEngine(){
		screen = new Screen(Settings.WIDTH, Settings.HEIGHT);
		init();
		// maybe make jacobi a runnable and start that thread and get control back here
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
		try {Thread.sleep(200);}catch(InterruptedException e){}
		while (Settings.RUNNING){
			update();
			render();
		}
	}

	public void update(){
		// buffer - temporary place for storage
	}

	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}

		screen.render();
		for (int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				draw(g, i, j);
			}
		}
		//g.drawImage(image, 0, 0, Settings.WIDTH, Settings.HEIGHT, null);
		g.dispose();
		bs.show();

	}

	private void draw(Graphics g, int i, int j){
		Region r = Merger.getUpdatedAlloy()[i][j];
		//Region r = Control.A[i][j];
		r.calcRGB();
		int red = (int) Math.round(r.red);
		int green = (int) Math.round(r.green);
		int blue = (int) Math.round(r.blue);
		g.setColor(new Color(red, green, blue));
		g.fillRect((i*Settings.SCALE), (j*Settings.SCALE), Settings.SCALE, Settings.SCALE);	
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