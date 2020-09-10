//Rupak Ravi
import javax.swing.JFrame;

public class Snake {
	
	public Snake() {
		JFrame frame = new JFrame();
		MainPanel panel = new MainPanel();
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Snake");
		frame.pack(); //Sets window and components to preferred size
		frame.setVisible(true);
      frame.setLocationRelativeTo(null); //Game opens in center of screen
	}
	
	
	public static void main(String[] args) {
		new Snake();
	}

}
