//Rupak Ravi
import java.awt.Graphics;

public class SnakeSegment {
	
	private int x, y, w, h;
	
	public SnakeSegment(int x, int y, int cell) {
		this.x = x;
		this.y = y;
		w = cell;
		h = cell;
	}
	
	public void tick() {
		
	}
	
	public void draw(Graphics g) {
		g.setColor(MainPanel.getSnakeColor());
		g.fillRect(x * w, y * h, w, h);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
