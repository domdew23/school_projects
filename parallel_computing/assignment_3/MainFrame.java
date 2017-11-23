import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
	public MainFrame(String title){
		super(title);
		// set layout manager
		setLayout(new BorderLayout());

		// create swing component
		final JTextArea textArea = new JTextArea();
		JButton button = new JButton("Click me");
		
		// add swing components to content pane
		Container c = getContentPane();
		c.add(textArea, BorderLayout.CENTER);
		c.add(button, BorderLayout.SOUTH);
		
		// add behavior
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				textArea.append("Hello\n");
			}
		});
	}
}