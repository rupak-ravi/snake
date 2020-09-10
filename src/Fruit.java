//Rupak Ravi
import java.awt.Graphics;

public class Fruit {
	
	private int x, y, w, h;
	
	public Fruit(int x, int y, int cellSize) {
		this.x = x;
		this.y = y;
		w = cellSize;
		h = cellSize;
	}
	
	public void tick() {
		
	}
	
	public void draw(Graphics g) {
		g.setColor(MainPanel.getFruitColor());
		g.fillRect(x * w, y * w, w, h);
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
