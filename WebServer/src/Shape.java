import java.io.PrintStream;

public abstract class Shape {
	protected int _x;
	protected int _y;
	protected String _color;
	protected int _changeX;
	protected int _changeY;
	
	public Shape (int x, int y, String color) {
		this._changeX = legalVelocity();
		this._changeY = legalVelocity();
		this._x = x;
		this._y = y;
		this._color = color;
	}
	
	//makes velocity random 
	public static int legalVelocity() {
		int randomVal = (int) (Math.random()*8) + 4;
		int operator = (int) (Math.random()*2) + 1;
		if (operator == 1) {
			randomVal = randomVal - (randomVal * 2);
		}
		return randomVal;
	}
	
	//makes x and Y change when advanceButton is pressed
	public void update() {
		this._x += this._changeX;
		this._y += this._changeY;
		if (bottom() >= 400) {
			this._changeY = -(this._changeY);
		}
		else if (top() <= 0) {
			this._changeY = this._changeY - (this._changeY*2);
		} 
		else if (right() >= 600) {
			this._changeX = -(this._changeX);
		}
		else if (left() <= 0) {
			this._changeX = this._changeX - (this._changeX*2);
		}

	}
	
	//making ball bounce from bottom wall
	public abstract int bottom();
	
	//making ball bounce from right wall
	public abstract int top();
	
	//making ball bounce from top wall
	public abstract int right();
	
	//making ball bounce from left wall
	public abstract int left();
	
	//that method is changing a color of the ball
	public void changeColor() {
		boolean isChangedColor = false;
		
		while(true) {
			int color = (int) (Math.random()*3) + 1;
			switch(color) {
				case 1:
					if (this._color.equals("red")) {
						break;
					}
					this._color = "red";
					isChangedColor = true;
					break;
				case 2: 
					if (this._color.equals("green")) {
						break;
					}
					this._color = "green";
					isChangedColor = true;
					break;
				case 3: 
					if (this._color.equals("blue")) {
						break;
					}
					this._color = "blue";
					isChangedColor = true;
					break;
			}
			if(isChangedColor)
				break;
		}
	}
	
	//rendering circle in HTML
	public void renderAsHTML(PrintStream out) {
		throw new UnsupportedOperationException(); // Subclasses implements
	}
}
