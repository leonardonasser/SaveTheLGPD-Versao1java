package aep.nasser.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener {

	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static int WIDTH = 480;
	public static int HEIGHT = 480;

	private BufferedImage image;

	public static Spritessheet sheet;
	private BufferedImage player;
	private int frames = 0;
	private int maxFrames = 20;
	private int curAnimation = 0, maxAnimation = 3;

	public static List<Inimigo> inimigo;
	public Spawner spawner;
	
	public static Rectangle maskBuraco;
	
	public static int mx,my;
	public static boolean isPressed = false;
	
	public static int score = 0;
	
	public Game() {
		sheet = new Spritessheet("/lgpd.png");
		player = sheet.getSprite(0, 0, 68, 68);
		
		sheet = new Spritessheet("/Inimigo2.png");
		
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.addMouseListener(this);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
		inimigo = new ArrayList<>();
		spawner = new Spawner();
		
		maskBuraco = new Rectangle(WIDTH/2 - 35, HEIGHT/2 - 35, 68,68);
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		
		JFrame frame = new JFrame();
		frame.setTitle("Save The LGPD");
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		
		new Thread(game).start();
	}

	public void tick() {
		frames++;
		if (frames > maxFrames) {
			frames =0;
			curAnimation++;
			if(curAnimation >= maxAnimation) {
				curAnimation = 0;
			}
		}
	}
	
	
	public void update() {
		spawner.update();
		
		for (int i = 0; i < inimigo.size(); i++) {
			inimigo.get(i).update();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();

		g.setColor(new Color(198,236,255));
		g.fillRect(0, 0,WIDTH,HEIGHT);

		// Redenrização jogo*/
		
		//LGPD
		g.drawImage(player,(WIDTH/2)-35,(HEIGHT/2)-35, null);

		//Inimigo
		for (int i = 0; i < inimigo.size(); i++) {
			inimigo.get(i).render(g);
		}
		
		//Score
		g.setColor(Color.BLACK);
		g.setFont(new Font("arial",Font.BOLD,22));
		g.drawString("Score: "+score, 10,20);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH,HEIGHT, null);
		bs.show();

	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				update();
				render();
				frames++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("Fps: " + frames);
				frames = 0;
				timer += 1000;
			}

		}
		stop();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		isPressed = true;
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}








