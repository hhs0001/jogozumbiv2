package jogozumbi;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;
public class Game extends Canvas implements Runnable {

	public static int WIDTH = 800, HEIGHT =608;
	public String title = "Jogo Zumbi";
	
	private Thread thread;
	private boolean isRunning = false;
	
	//instances
	private Handler handler;
	private KeyInput input;
	private MouseInput minput;
	
	public Game() {
		new Window(WIDTH, HEIGHT, title, this);
		start();
		
		init();
	}
	    
	private void init() {
		handler = new Handler();
		input = new KeyInput();
		minput = new MouseInput(handler);
		this.addKeyListener(input);
		this.addMouseListener(minput);
		
		handler.addObject(new Player(100, 100, ID.Player, input));
		handler.addObject(new Zombie(new Random().nextInt(800 + 1), new Random().nextInt(608 + 1), ID.Zombie));
		minput.findPlayer();
	}
	    
	private synchronized void start() {
		if(isRunning) return;
		
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}
	
	private synchronized void stop() {
		if(!isRunning) return;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isRunning = false;
	}
	//gameloop
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
					
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		
	}
	
	private void tick() {
		//updates the game
		handler.tick();
	}
	
	private void render() {
		//renders the game
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		
		bs.show();
		g.dispose();
		
	}
	
	public static void main(String args[]) {
		new Game();
	}
}
