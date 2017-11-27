import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Canvas;
import java.util.concurrent.ThreadLocalRandom;

public class Control extends Canvas implements Runnable{
	private Settings s;
	private boolean startRandom = false;
	private Thread thread;
	private JFrame f;

	public Control(Settings settings){
		this.s = settings;
		Region[][] alloy = initAlloy(s.C1, s.C2, s.C3, s.S, s.T, s.width, s.height);
		
		Region[][] A = initAlloy(s.C1, s.C2, s.C3, s.S, s.T, s.width, s.height);
		Region[][] B = new Region[s.width][s.height];
		Jacobi j = new Jacobi(A, B, 0, s.height, 0, s.width, 1, s.threshold);
		
		//j.compute();
		display(A);
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
				JFrame frame = new JFrame("Alloy"); // main window of the application
				GraphicsModule g = new GraphicsModule(s.width, s.height, s.scale, A);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(g);
				frame.setSize((s.width*(2*s.scale)), (s.height*(2*s.scale)));
				frame.setVisible(true);
			}
		});
	}


	private Region[][] initAlloy(double C1, double C2, double C3, double S, double T, int width, int height){
		Region[][] alloy = new Region[width][height];

		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				Region r = new Region(C1, C2, C3, i, j);
				alloy[i][j] = r;
				r.setTemp(1.0);
			}
		}

		alloy[0][0].setTemp(S);
		alloy[width-1][height-1].setTemp(T);
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				Region left=null,top=null,right=null,bottom=null;
				if (i-1 >= 0){
					left = alloy[i-1][j];
				}
				if (i+1 < width){
					right = alloy[i+1][j];
				}
				if (j-1 >= 0){
					//top
					top = alloy[i][j-1];
				}
				if (j+1 < height){
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