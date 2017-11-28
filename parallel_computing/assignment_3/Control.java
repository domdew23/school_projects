import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Canvas;
import java.util.concurrent.ThreadLocalRandom;

public class Control extends Canvas implements Runnable{
	private boolean startRandom = false;
	private Thread thread;
	private JFrame frame;

	public Control(){
		Region[][] A = initAlloy();
		Region[][] B = new Region[Settings.WIDTH][Settings.HEIGHT];

		Jacobi j = new Jacobi(A, B, 0, Settings.HEIGHT, 0, Settings.WIDTH, Settings.MAX_STEPS, Settings.THRESHOLD);
		// maybe make jacobi a runnable and start that thread and get control back here
		j.compute();
		display(Merger.getUpdatedAlloy());
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

	public void run(){

	}

	private void display(Region[][] A){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				frame = new JFrame(); // main window of the application
				frame.setResizable(false);
				frame.setTitle("Alloy");
				GraphicsModule g = new GraphicsModule(Settings.WIDTH, Settings.HEIGHT, Settings.SCALE, A);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(g);
				frame.setSize((Settings.WIDTH*(2*Settings.SCALE)), (Settings.HEIGHT*(2*Settings.SCALE)));
				frame.setVisible(true);
			}
		});
	}


	private Region[][] initAlloy(){
		Region[][] alloy = new Region[Settings.WIDTH][Settings.HEIGHT];

		for (int i = 0; i < Settings.WIDTH; i++){
			for (int j = 0; j < Settings.HEIGHT; j++){
				Region r = new Region(Settings.C1, Settings.C2, Settings.C3, i, j);
				alloy[i][j] = r;
				r.setTemp(1.0);
			}
		}

		alloy[0][0].setTemp(Settings.S);
		alloy[Settings.WIDTH-1][Settings.HEIGHT-1].setTemp(Settings.T);
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
					//top
					top = alloy[i][j-1];
				}
				if (j+1 < Settings.HEIGHT){
					bottom = alloy[i][j+1];
				}
				alloy[i][j].setNeighbors(left, top, right, bottom);
				
				if (startRandom){
					alloy[i][j].setTemp(ThreadLocalRandom.current().nextDouble(-320.0, 320.0));
				}
			}
		}
		return alloy;
	}
}