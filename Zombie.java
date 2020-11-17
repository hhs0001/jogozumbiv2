package jogozumbi;

import java.awt.Color;
import java.awt.Graphics;

public class Zombie extends GameObject{
	
	private float _acc = 1f;
	private float _dcc = 0.5f;
	
	public Zombie(float x, float y, ID id) {
		super(x, y, id);
	}

	@Override
	public void tick() {
		x += velX;
		y += velY;
		
		velX = clamp(velX, 4, -4);
		velY = clamp(velY, 4 ,-4);
	}
	
	private float clamp(float value, float max, float min) {
		if(value >= max) value = max;
		else if(value <= min) value = min;
		return value;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect((int)x, (int)y, 32, 32);
	}

}
