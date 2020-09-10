//Rupak Ravi
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable, KeyListener, MouseListener{

	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	private boolean running, up, down, left, right, gameOver, colliding;
	private Thread t1;
	private SnakeSegment segment;
	private ArrayList<SnakeSegment> snake;
	private Fruit fruit;
	private ArrayList<Fruit> fruits;
	private int x, y, size, ticks, tickTime;
	private enum STATE{MENU, GAME, CONTROLS, SETTINGS, COLORS};
	private STATE state;
	private Font titleFont, normalFont, smallFont;
    private static Color fruitColor, snakeColor, bgColor, textColor;
	
	public MainPanel() {
		//Default speed
		tickTime = 1000000;
      	//Default colors
      	fruitColor = new Color(255, 0, 0); //Red
      	snakeColor = new Color(255, 255, 255); //White
      	bgColor = new Color(0, 0, 0); //Black
      	textColor = new Color(255, 0, 0); //Red
		init();
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		addMouseListener(this);
		start();
	}
	
	public void init() {
		state = STATE.MENU;
		//Starting position
		x = 10;
		y = 10;
		//Starting size
		size = 1;
		//Snake starts at rest
		up = false;
		down = false;
		right = false;
		left = false;
		gameOver = false;
		snake = new ArrayList<SnakeSegment>();
		fruits = new ArrayList<Fruit>();
		titleFont = new Font("Arial", Font.BOLD, 50);
		normalFont = new Font("Arial", Font.BOLD, 30);
		smallFont = new Font("Arial", Font.BOLD, 20);
	}
	
	public void start() {
		if(running)
			return;
		running = true;
		t1 = new Thread(this);
		t1.start();
	}
	
	public void stop() {
		if(!running)
			return;
		running = false;
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		if(state == STATE.GAME) {
			//Snake starts out as one segment that is 10 pixels wide
			if(snake.size() == 0) {
				segment = new SnakeSegment(x, y, 10);
				snake.add(segment);
			}
			ticks++;
			//Checks user input every 1000000 nanoseconds and moves snake accordingly
			if(ticks > tickTime) {
				if(right)
					x++;
				if(left)
					x--;
				if(up)
					y--;
				if(down)
					y++;
				ticks = 0;
				segment = new SnakeSegment(x, y, 10);
				//Simulates motion by constantly removing and adding segments to the snake
				snake.add(segment);
				if(snake.size() > size) {
					snake.remove(0);
				}
			}
			//Generates a fruit in a random location
			if(fruits.size() == 0) {
				int x = (int)(Math.random() * (WIDTH / 10 - 1));
				int y = (int)(Math.random() * (HEIGHT / 10 - 1));
				//Prevents a fruit from spawning on the body of the snake
				colliding = true;
				while(colliding) {
					colliding = false;
					for(SnakeSegment s: snake) {
						if (x == s.getX() && y == s.getY()) {
							colliding = true;
							x = (int)(Math.random() * (WIDTH / 10 - 1));
							y = (int)(Math.random() * (HEIGHT / 10 - 1));
							break;
						}
					}
				}
				fruit = new Fruit(x, y, 10);
				fruits.add(fruit);
			}
			//Collision detection with fruit, makes snake longer and removes the current fruit
			for(int i = 0; i < fruits.size(); i++) {
				if(x == fruits.get(i).getX() && y == fruits.get(i).getY()) {
					size++;
					fruits.remove(i);
				}
			}
			//Wall collision detection
			if(x < 0 || x > (WIDTH / 10 - 1) || y < 0 || y > (HEIGHT / 10 - 1)) {
				gameOver = true;
				stop();
			}
			//Body collision detection
			for(int j = 0; j < snake.size(); j++) {
				if((x == snake.get(j).getX() && y == snake.get(j).getY()) && size > 1) {
					//Prevents game from stopping immediately after eating a fruit
					if(j != snake.size() - 1) {
						gameOver = true;
						stop();
					}
				}
			}
		}
	}
	
	public void paint(Graphics g) {
		if(state == STATE.MENU) {
			g.clearRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setFont(titleFont);
			g.setColor(Color.WHITE);
			g.drawString("SNAKE", WIDTH / 2 - 90, 75);
			g.setFont(normalFont);
			g.drawString("By Rupak Ravi", WIDTH / 2 - 100, 455);
			//Play button
			g.drawRect(WIDTH / 2 - 215, 135, 200, 100);
			g.drawString("Play", WIDTH / 2 - 145, 195);
			//Settings button
			g.drawRect(WIDTH / 2 + 15, 135, 200, 100);
			g.drawString("Settings", WIDTH / 2 + 60, 195);
			//Help button
			g.drawRect(WIDTH / 2 - 215, 265, 200, 100);
			g.drawString("Help", WIDTH / 2 - 145, 325);
			//Quit button
			g.drawRect(WIDTH / 2 + 15, 265, 200, 100);
			g.drawString("Quit", WIDTH / 2 + 85, 325);
		}
		else if(state == STATE.CONTROLS) {
			g.clearRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setFont(titleFont);
			g.setColor(Color.WHITE);
			g.drawString("CONTROLS", WIDTH / 2 - 140, 75);
			g.setFont(smallFont);
			g.drawString("Arrow Keys = Movement", WIDTH / 2 - 115, 140);
			g.drawString("Spacebar (While Playing) = Pause", WIDTH / 2 - 155, 190);
			g.drawString("Spacebar (Game Over) = Return to Menu", WIDTH / 2 - 190, 240);
         g.drawString("E (Game Over) = Restart Game", WIDTH / 2 - 145, 290);
			//Back button
			g.setFont(normalFont);
			g.drawRect(WIDTH / 2 - 100, 350, 200, 100);
			g.drawString("Back", WIDTH / 2 - 35, 410);
		}
		else if(state == STATE.SETTINGS) {
			g.clearRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setFont(titleFont);
			g.setColor(Color.WHITE);
			g.drawString("SETTINGS", WIDTH / 2 - 130, 75);
			g.setFont(normalFont);
			//Normal button
			g.drawRect(WIDTH / 2 - 215, 135, 200, 100);
			g.drawString("Normal", WIDTH / 2 - 165, 195);
			//Hard button
			g.drawRect(WIDTH / 2 + 15, 135, 200, 100);
			g.drawString("Hard", WIDTH / 2 + 80, 195);
			//Very Hard button
			g.drawRect(WIDTH / 2 - 215, 265, 200, 100);
			g.drawString("Very Hard", WIDTH / 2 - 185, 325);
	        //Color button
	        g.drawRect(WIDTH / 2 + 15, 265, 200, 100);
	        g.drawString("Colors", WIDTH / 2 + 70, 325);
	        //Back button
	        g.drawRect(WIDTH / 2 - 100, 385, 200, 100);
			g.drawString("Back", WIDTH / 2 - 35, 445);
		}
      else if(state == STATE.COLORS) {
         g.clearRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setFont(titleFont);
			g.setColor(Color.WHITE);
			g.drawString("COLOR SETTINGS", WIDTH / 2 - 220, 75);
         g.setFont(normalFont);
         //Snake color button
			g.drawRect(WIDTH / 2 - 215, 135, 200, 100);
			g.drawString("Snake", WIDTH / 2 - 160, 195);
			//Fruit color button
			g.drawRect(WIDTH / 2 + 15, 135, 200, 100);
			g.drawString("Fruit", WIDTH / 2 + 80, 195);
			//BG color button
			g.drawRect(WIDTH / 2 - 215, 265, 200, 100);
			g.drawString("Background", WIDTH / 2 - 200, 325);
         //Text color button
         g.drawRect(WIDTH / 2 + 15, 265, 200, 100);
         g.drawString("Text", WIDTH / 2 + 80, 325);
         //Back button
			g.drawRect(WIDTH / 2 - 100, 385, 200, 100);
			g.drawString("Back", WIDTH / 2 - 35, 445);
      }
		else if(state == STATE.GAME) {
			g.clearRect(0,  0 , WIDTH, HEIGHT);
			g.setColor(getBGColor());
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			//Draws the snake by traversing the array of its segments (for loop avoids ConcurrentModificationException)
			for(int k = 0; k < snake.size(); k++) {
				snake.get(k).draw(g);
			}
			for(int l = 0; l < fruits.size(); l++) {
				fruits.get(l).draw(g);
			}
			if(!running && gameOver) {
				g.setColor(getTextColor());
            g.drawString("Game Over", WIDTH / 2 - 50, HEIGHT / 2);
				g.drawString("Score: " + (size - 1), WIDTH / 2 - 35, HEIGHT / 2 + 20);
			}
			else if(!running && !gameOver) {
				g.setColor(getTextColor());
            g.drawString("Paused", WIDTH / 2 - 30, HEIGHT / 2);
			}
		}
	}
	
	//Constantly updates the game panel while the game is running
	public void run() {
		while(running) {
			tick();
			repaint();
		}
	}

	public void keyPressed(KeyEvent e) {
		if(state == STATE.GAME) {
			int key = e.getKeyCode();
			//If up is pressed
			if(key == KeyEvent.VK_UP && !down) {
				up = true;
				left = false;
				right = false;
			}
			//If down is pressed
			if(key == KeyEvent.VK_DOWN && !up) {
				down = true;
				left = false;
				right = false;
			}
			//If right is pressed
			if(key == KeyEvent.VK_RIGHT && !left) {
				right = true;
				up = false;
				down = false;
			}
			//If left is pressed
			if(key == KeyEvent.VK_LEFT && !right) {
				left = true;
				up = false;
				down = false;
			}
			//If space is pressed
			if(key == KeyEvent.VK_SPACE) {
				if(gameOver) {
					init();
					start();
				}
				else if (!gameOver && running)
					stop();
				else if (!gameOver && !running)
					start();
			}
			//If E is pressed
			if(key == KeyEvent.VK_E) {
				if(gameOver) {
					//Starting position
					x = 10;
					y = 10;
					//Starting size
					size = 1;
					//Snake starts at rest
					up = false;
					down = false;
					right = false;
					left = false;
					gameOver = false;
					snake = new ArrayList<SnakeSegment>();
					fruits = new ArrayList<Fruit>();
					titleFont = new Font("Arial", Font.BOLD, 50);
					normalFont = new Font("Arial", Font.BOLD, 30);
					smallFont = new Font("Arial", Font.BOLD, 20);
					start();
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		if(state == STATE.MENU) {
			//Play button
			if(mx >= WIDTH / 2 - 215 && mx <= WIDTH / 2 - 15) {
				if(my >= 135 && my <= 235) {
               state = STATE.GAME;
				}
			}
			//Settings button
			if(mx >= WIDTH / 2 + 15 && mx <= WIDTH / 2 + 215) {
				if(my >= 135 && my <= 235) {
					state = STATE.SETTINGS;
				}
			}
			//Controls button
			if(mx >= WIDTH / 2 - 215 && mx <= WIDTH / 2 - 15) {
				if(my >= 265 && my <= 365) {
					state = STATE.CONTROLS;
				}
			}
			//Quit button
			if(mx >= WIDTH / 2 + 15 && mx <= WIDTH / 2 + 215) {
				if(my >= 265 && my <= 365) {
					System.exit(1);
				}
			}
		}
		else if(state == STATE.CONTROLS) {
			//Back button
			if(mx >= WIDTH / 2 - 100 && mx <= WIDTH / 2 + 100) {
				if(my >= 350 && my <= 450) {
					state = STATE.MENU;
				}
			}
		}
		else if(state == STATE.SETTINGS) {
			//Normal button
			if(mx >= WIDTH / 2 - 215 && mx <= WIDTH / 2 + 15) {
				if(my >= 135 && my <= 235) {
					tickTime = 1000000;
					state = STATE.MENU;
				}
			}
			//Hard button
			if(mx >= WIDTH / 2 + 15 && mx <= WIDTH / 2 + 215) {
				if(my >= 135 && my <= 235) {
					tickTime = 500000;
					state = STATE.MENU;
				}
			}
			//Very Hard button
			if(mx >= WIDTH / 2 - 215 && mx <= WIDTH / 2 + 15) {
				if(my >= 265 && my <= 365) {
					tickTime = 250000;
					state = STATE.MENU;
				}
			}
         //Colors button
			if(mx >= WIDTH / 2 + 15 && mx <= WIDTH / 2 + 215) {
				if(my >= 265 && my <= 365) {
					state = STATE.COLORS;
				}
			}
		 //Back button
			if(mx >= WIDTH / 2 - 100 && mx <= WIDTH / 2 + 100) {
				if(my >= 385 && my <= 485) {
				   state = STATE.MENU;
				}
			}
		}
      else if(state == STATE.COLORS) {
         //Snake color button
			if(mx >= WIDTH / 2 - 215 && mx <= WIDTH / 2 + 15) {
				if(my >= 135 && my <= 235) {
					snakeColor = JColorChooser.showDialog(null, "Pick your color", Color.WHITE);
				}
			}
			//Fruit color button
			if(mx >= WIDTH / 2 + 15 && mx <= WIDTH / 2 + 215) {
				if(my >= 135 && my <= 235) {
				   fruitColor = JColorChooser.showDialog(null, "Pick your color", Color.RED);
            }
			}
			//BG color button
			if(mx >= WIDTH / 2 - 215 && mx <= WIDTH / 2 + 15) {
				if(my >= 265 && my <= 365) {
				   bgColor = JColorChooser.showDialog(null, "Pick your color", Color.BLACK);
            }
			}
         //Text color button
			if(mx >= WIDTH / 2 + 15 && mx <= WIDTH / 2 + 215) {
				if(my >= 265 && my <= 365) {
				   textColor = JColorChooser.showDialog(null, "Pick your color", Color.RED);
            }
			}
         //Back button
			if(mx >= WIDTH / 2 - 100 && mx <= WIDTH / 2 + 100) {
				if(my >= 385 && my <= 485) {
				   state = STATE.SETTINGS;
            }
			}
      }
	}

	public void mouseReleased(MouseEvent e) {
		
	}
   
   public static Color getSnakeColor(){
      return snakeColor;
   }
   
   public static Color getFruitColor(){
      return fruitColor;
   }
   
   public static Color getBGColor(){
      return bgColor;
   }
   
   public static Color getTextColor(){
      return textColor;
   }
}
